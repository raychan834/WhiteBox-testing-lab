package com.Praktikum.Whitebox.service;

import com.praktikum.whitebox.model.Produk;
import com.praktikum.whitebox.repository.RepositoryProduk;
import com.praktikum.whitebox.service.ServiceInventaris;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Service Inventaris dengan Mocking")
public class ServiceInventarisTest {
    @Mock
    private RepositoryProduk mockRepositoryProduk;

    private ServiceInventaris serviceInventaris;
    private Produk produkTest;

    @BeforeEach
    void setUp() {
        serviceInventaris = new ServiceInventaris(mockRepositoryProduk);
        produkTest = new Produk("PROD001", "Laptop Gaming", "Elektronik",
                15000000, 10, 5);
    }

    // ===== TEST CASES YANG SUDAH ADA (SEBELUM TUGAS 2) =====

    @Test
    @DisplayName("Tambah produk berhasil - semua kondisi valid")
    void testTambahProdukBerhasil() {
        // Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());
        when(mockRepositoryProduk.simpan(produkTest)).thenReturn(true);

        // Act
        boolean hasil = serviceInventaris.tambahProduk(produkTest);

        // Assert
        assertTrue(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk).simpan(produkTest);
    }

    @Test
    @DisplayName("Tambah produk gagal - produk sudah ada")
    void testTambahProdukGagalSudahAda() {
        // Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        // Act
        boolean hasil = serviceInventaris.tambahProduk(produkTest);

        // Assert
        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk, never()).simpan(any(Produk.class));
    }

    @Test
    @DisplayName("Keluar stok berhasil - stok mencukupi")
    void testKeluarStokBerhasil() {
        // Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 5)).thenReturn(true);

        // Act
        boolean hasil = serviceInventaris.keluarStok("PROD001", 5);

        // Assert
        assertTrue(hasil);
        verify(mockRepositoryProduk).updateStok("PROD001", 5);
    }

    @Test
    @DisplayName("Keluar stok gagal - stok tidak mencukupi")
    void testKeluarStokGagalStokTidakMencukupi() {
        // Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        // Act
        boolean hasil = serviceInventaris.keluarStok("PROD001", 15);

        // Assert
        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    @Test
    @DisplayName("Hitung total nilai inventaris")
    void testHitungTotalNilaiInventaris() {
        // Arrange
        Produk produk1 = new Produk("PROD001", "Laptop", "Elektronik", 10000000, 2, 1);
        Produk produk2 = new Produk("PROD002", "Mouse", "Elektronik", 500000, 5, 2);
        Produk produkNonAktif = new Produk("PROD003", "Keyboard", "Elektronik", 300000, 3, 1);
        produkNonAktif.setAktif(false);

        List<Produk> semuaProduk = Arrays.asList(produk1, produk2, produkNonAktif);
        when(mockRepositoryProduk.cariSemua()).thenReturn(semuaProduk);

        // Act
        double totalNilai = serviceInventaris.hitungTotalNilaiInventaris();

        // Assert
        double expected = (10000000 * 2) + (500000 * 5); // hanya produk aktif
        assertEquals(expected, totalNilai, 0.001);
        verify(mockRepositoryProduk).cariSemua();
    }

    @Test
    @DisplayName("Get produk stok menipis")
    void testGetProdukStokMenipis() {
        // Arrange
        Produk produkStokAman = new Produk("PROD001", "Laptop", "Elektronik", 10000000, 10, 5);
        Produk produkStokMenipis = new Produk("PROD002", "Mouse", "Elektronik", 500000, 3, 5);

        List<Produk> produkMenipis = Collections.singletonList(produkStokMenipis);
        when(mockRepositoryProduk.cariProdukStokMenipis()).thenReturn(produkMenipis);

        // Act
        List<Produk> hasil = serviceInventaris.getProdukStokMenipis();

        // Assert
        assertEquals(1, hasil.size());
        assertEquals("PROD002", hasil.get(0).getKode());
        verify(mockRepositoryProduk).cariProdukStokMenipis();
    }

    // ==================== TUGAS 2: TEST CASES TAMBAHAN ====================
    // ========== (BOUNDARY VALUE ANALYSIS + PATH COVERAGE) ==========

    // ✅ TAMBAHAN 1: Boundary Values untuk updateStok
    @Test
    @DisplayName("Test boundary values untuk updateStok - nilai batas stok")
    void testBoundaryValuesUpdateStok() {
        // Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        // Gunakan eq() untuk specific values dan anyInt() untuk any value
        when(mockRepositoryProduk.updateStok(eq("PROD001"), anyInt())).thenReturn(true);

        // Test stok = 0 (batas bawah valid)
        boolean hasil1 = serviceInventaris.updateStok("PROD001", 0);
        assertTrue(hasil1);

        // Test stok = 1 (tepat di atas batas bawah)
        boolean hasil2 = serviceInventaris.updateStok("PROD001", 1);
        assertTrue(hasil2);

        // Test stok = -1 (batas bawah invalid)
        boolean hasil3 = serviceInventaris.updateStok("PROD001", -1);
        assertFalse(hasil3);
    }

    // ✅ TAMBAHAN 2: Boundary Values untuk kode produk valid (Parameterized Test)
    @ParameterizedTest
    @DisplayName("Test boundary values untuk kode produk validasi")
    @ValueSource(strings = {"ABC", "ABCDEFGHIJ", "ABC123", "123ABC"}) // batas 3-10 karakter
    void testBoundaryValuesKodeProdukValid(String kode) {
        // Arrange
        Produk produk = new Produk(kode, "Test Produk", "Elektronik", 100000, 10, 5);
        when(mockRepositoryProduk.cariByKode(kode)).thenReturn(Optional.empty());
        when(mockRepositoryProduk.simpan(produk)).thenReturn(true);

        // Act & Assert
        assertTrue(serviceInventaris.tambahProduk(produk));
    }

    // ✅ TAMBAHAN 3: Boundary Values untuk kode produk invalid (Parameterized Test)
    @ParameterizedTest
    @DisplayName("Test boundary values untuk kode produk invalid")
    @ValueSource(strings = {"AB", "ABCDEFGHIJK", "", "   "}) // kurang dari 3 atau lebih dari 10 karakter
    void testBoundaryValuesKodeProdukInvalid(String kode) {
        // Arrange
        Produk produk = new Produk(kode, "Test Produk", "Elektronik", 100000, 10, 5);

        // Act & Assert
        assertFalse(serviceInventaris.tambahProduk(produk));
    }

    // ✅ TAMBAHAN 4: Path Coverage untuk hapusProduk
    @Test
    @DisplayName("Test semua paths dalam hapusProduk")
    void testAllPathsHapusProduk() {
        // Path 1: Kode invalid → return false
        assertFalse(serviceInventaris.hapusProduk(""));

        // Path 2: Produk tidak ditemukan → return false
        when(mockRepositoryProduk.cariByKode("PROD999")).thenReturn(Optional.empty());
        assertFalse(serviceInventaris.hapusProduk("PROD999"));

        // Path 3: Produk ditemukan tapi stok > 0 → return false
        Produk produkStokAda = new Produk("PROD001", "Laptop", "Elektronik", 100000, 5, 2);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkStokAda));
        assertFalse(serviceInventaris.hapusProduk("PROD001"));

        // Path 4: Produk ditemukan dan stok = 0 → return true
        Produk produkStokHabis = new Produk("PROD002", "Mouse", "Elektronik", 50000, 0, 2);
        when(mockRepositoryProduk.cariByKode("PROD002")).thenReturn(Optional.of(produkStokHabis));
        when(mockRepositoryProduk.hapus("PROD002")).thenReturn(true);
        assertTrue(serviceInventaris.hapusProduk("PROD002"));
    }

    // ✅ TAMBAHAN 5: Path Coverage untuk keluarStok
    @Test
    @DisplayName("Test semua paths dalam keluarStok")
    void testAllPathsKeluarStok() {
        // Path 1: Parameter invalid → return false
        assertFalse(serviceInventaris.keluarStok("", 5));
        assertFalse(serviceInventaris.keluarStok("PROD001", 0));
        assertFalse(serviceInventaris.keluarStok("PROD001", -1));

        // Path 2: Produk tidak ditemukan → return false
        when(mockRepositoryProduk.cariByKode("PROD999")).thenReturn(Optional.empty());
        assertFalse(serviceInventaris.keluarStok("PROD999", 5));

        // Path 3: Produk tidak aktif → return false
        Produk produkNonAktif = new Produk("PROD001", "Laptop", "Elektronik", 100000, 10, 5);
        produkNonAktif.setAktif(false);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkNonAktif));
        assertFalse(serviceInventaris.keluarStok("PROD001", 5));

        // Path 4: Stok tidak mencukupi → return false
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        assertFalse(serviceInventaris.keluarStok("PROD001", 15));

        // Path 5: Semua kondisi terpenuhi → return true
        when(mockRepositoryProduk.updateStok("PROD001", 5)).thenReturn(true);
        assertTrue(serviceInventaris.keluarStok("PROD001", 5));
    }

    // ✅ TAMBAHAN 6: Coverage untuk method getProdukStokHabis
    @Test
    @DisplayName("Test getProdukStokHabis")
    void testGetProdukStokHabis() {
        // Arrange
        Produk produkStokHabis = new Produk("PROD002", "Mouse", "Elektronik", 50000, 0, 5);
        List<Produk> stokHabisList = Collections.singletonList(produkStokHabis);
        when(mockRepositoryProduk.cariProdukStokHabis()).thenReturn(stokHabisList);

        // Act
        List<Produk> hasil = serviceInventaris.getProdukStokHabis();

        // Assert
        assertEquals(1, hasil.size());
        assertEquals(0, hasil.get(0).getStok());
        verify(mockRepositoryProduk).cariProdukStokHabis();
    }

    // ✅ TAMBAHAN 7: Coverage untuk method cariProdukByKategori
    @Test
    @DisplayName("Test cariProdukByKategori")
    void testCariProdukByKategori() {
        // Arrange
        List<Produk> produkList = Arrays.asList(produkTest);
        when(mockRepositoryProduk.cariByKategori("Elektronik")).thenReturn(produkList);

        // Act
        List<Produk> hasil = serviceInventaris.cariProdukByKategori("Elektronik");

        // Assert
        assertEquals(1, hasil.size());
        assertEquals("PROD001", hasil.get(0).getKode());
        verify(mockRepositoryProduk).cariByKategori("Elektronik");
    }

    // ✅ TAMBAHAN 8: Parameterized Test untuk masukStok dengan berbagai skenario
    @ParameterizedTest
    @DisplayName("Test masukStok dengan berbagai skenario")
    @CsvSource({
            "PROD001, 5, true",   // normal case
            "PROD001, 0, false",  // jumlah 0
            "PROD001, -1, false", // jumlah negatif
            "INVALID, 5, false"   // kode invalid
    })
    void testMasukStokVariousScenarios(String kode, int jumlah, boolean expected) {
        // Arrange
        if (expected) {
            when(mockRepositoryProduk.cariByKode(kode)).thenReturn(Optional.of(produkTest));
            when(mockRepositoryProduk.updateStok(kode, produkTest.getStok() + jumlah)).thenReturn(true);
        }

        // Act & Assert
        assertEquals(expected, serviceInventaris.masukStok(kode, jumlah));
    }
    // ✅ TAMBAHAN 9: Test untuk hitungTotalStok (0% coverage)
    @Test
    @DisplayName("Test hitungTotalStok - berbagai skenario")
    void testHitungTotalStok() {
        // Arrange
        Produk produkAktif1 = new Produk("PROD001", "Laptop", "Elektronik", 10000000, 5, 2);
        Produk produkAktif2 = new Produk("PROD002", "Mouse", "Elektronik", 500000, 3, 1);
        Produk produkNonAktif = new Produk("PROD003", "Keyboard", "Elektronik", 300000, 2, 1);
        produkNonAktif.setAktif(false);

        List<Produk> semuaProduk = Arrays.asList(produkAktif1, produkAktif2, produkNonAktif);
        when(mockRepositoryProduk.cariSemua()).thenReturn(semuaProduk);

        // Act
        int totalStok = serviceInventaris.hitungTotalStok();

        // Assert
        assertEquals(8, totalStok); // 5 + 3 (hanya produk aktif)
        verify(mockRepositoryProduk).cariSemua();
    }

    @Test
    @DisplayName("Test hitungTotalStok - tidak ada produk")
    void testHitungTotalStok_NoProducts() {
        // Arrange
        when(mockRepositoryProduk.cariSemua()).thenReturn(Collections.emptyList());

        // Act
        int totalStok = serviceInventaris.hitungTotalStok();

        // Assert
        assertEquals(0, totalStok);
        verify(mockRepositoryProduk).cariSemua();
    }

    // ✅ TAMBAHAN 10: Test untuk cariProdukByKode (0% coverage)
    @Test
    @DisplayName("Test cariProdukByKode - kode valid dan produk ditemukan")
    void testCariProdukByKode_ValidFound() {
        // Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        // Act
        Optional<Produk> hasil = serviceInventaris.cariProdukByKode("PROD001");

        // Assert
        assertTrue(hasil.isPresent());
        assertEquals("PROD001", hasil.get().getKode());
        verify(mockRepositoryProduk).cariByKode("PROD001");
    }

    @Test
    @DisplayName("Test cariProdukByKode - kode invalid")
    void testCariProdukByKode_InvalidKode() {
        // Act
        Optional<Produk> hasil = serviceInventaris.cariProdukByKode("");

        // Assert
        assertFalse(hasil.isPresent());
        verify(mockRepositoryProduk, never()).cariByKode(anyString());
    }

    @Test
    @DisplayName("Test cariProdukByKode - kode valid tapi produk tidak ditemukan")
    void testCariProdukByKode_ValidNotFound() {
        // Arrange
        when(mockRepositoryProduk.cariByKode("PROD999")).thenReturn(Optional.empty());

        // Act
        Optional<Produk> hasil = serviceInventaris.cariProdukByKode("PROD999");

        // Assert
        assertFalse(hasil.isPresent());
        verify(mockRepositoryProduk).cariByKode("PROD999");
    }

    // ✅ TAMBAHAN 11: Test untuk cariProdukByNama (0% coverage)
    @Test
    @DisplayName("Test cariProdukByNama")
    void testCariProdukByNama() {
        // Arrange
        List<Produk> produkList = Arrays.asList(produkTest);
        when(mockRepositoryProduk.cariByNama("Laptop")).thenReturn(produkList);

        // Act
        List<Produk> hasil = serviceInventaris.cariProdukByNama("Laptop");

        // Assert
        assertEquals(1, hasil.size());
        assertEquals("PROD001", hasil.get(0).getKode());
        verify(mockRepositoryProduk).cariByNama("Laptop");
    }

    @Test
    @DisplayName("Test cariProdukByNama - tidak ada hasil")
    void testCariProdukByNama_NoResults() {
        // Arrange
        when(mockRepositoryProduk.cariByNama("Nonexistent")).thenReturn(Collections.emptyList());

        // Act
        List<Produk> hasil = serviceInventaris.cariProdukByNama("Nonexistent");

        // Assert
        assertTrue(hasil.isEmpty());
        verify(mockRepositoryProduk).cariByNama("Nonexistent");
    }
    // ✅ TAMBAHAN 12: Improve coverage untuk updateStok
    @Test
    @DisplayName("Test updateStok - stokBaru = 0 (boundary value)")
    void testUpdateStok_StokNol() {
        // Arrange
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 0)).thenReturn(true);

        // Act
        boolean hasil = serviceInventaris.updateStok("PROD001", 0);

        // Assert
        assertTrue(hasil);
        verify(mockRepositoryProduk).updateStok("PROD001", 0);
    }

    @Test
    @DisplayName("Test updateStok - produk tidak ditemukan")
    void testUpdateStok_ProdukNotFound() {
        // Arrange
        when(mockRepositoryProduk.cariByKode("PROD999")).thenReturn(Optional.empty());

        // Act
        boolean hasil = serviceInventaris.updateStok("PROD999", 10);

        // Assert
        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    @ParameterizedTest
    @DisplayName("Test updateStok dengan berbagai nilai stok boundary")
    @CsvSource({
            "0, true",      // batas bawah valid
            "1, true",      // tepat di atas batas bawah
            "1000, true",   // nilai besar
            "-1, false"     // batas bawah invalid
    })
    void testUpdateStok_BoundaryValues(int stokBaru, boolean expected) {
        // Arrange
        if (expected) {
            when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
            when(mockRepositoryProduk.updateStok("PROD001", stokBaru)).thenReturn(true);
        }

        // Act & Assert
        assertEquals(expected, serviceInventaris.updateStok("PROD001", stokBaru));
    }
    // ✅ TAMBAHAN 13: Improve branch coverage untuk masukStok
    @Test
    @DisplayName("Test masukStok - produk tidak aktif")
    void testMasukStok_ProdukNonAktif() {
        // Arrange
        Produk produkNonAktif = new Produk("PROD001", "Laptop", "Elektronik", 15000000, 10, 5);
        produkNonAktif.setAktif(false);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkNonAktif));

        // Act
        boolean hasil = serviceInventaris.masukStok("PROD001", 5);

        // Assert
        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    @Test
    @DisplayName("Test masukStok - kode null")
    void testMasukStok_KodeNull() {
        // Act
        boolean hasil = serviceInventaris.masukStok(null, 5);

        // Assert
        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).cariByKode(anyString());
    }
}