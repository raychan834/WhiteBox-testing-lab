package com.Praktikum.Whitebox.model;

import com.praktikum.whitebox.model.Produk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Class Produk - White Box Testing")
public class ProdukTest {
    private Produk produk;

    @BeforeEach
    void setUp() {
        produk = new Produk("PROD001",
                "Laptop Gaming",
                "Elektronik",
                15000000,
                10,
                5);
    }

    // ==================== TEST CASES YANG SUDAH ADA ====================

    @Test
    @DisplayName("Test status stok - stok aman")
    void testStokAman() {
        produk.setStok(10);
        produk.setStokMinimum(5);
        assertTrue(produk.isStokAman());
        assertFalse(produk.isStokMenipis());
        assertFalse(produk.isStokHabis());
    }

    @Test
    @DisplayName("Test status stok - stok menipis")
    void testStokMenipis() {
        produk.setStok(5);
        produk.setStokMinimum(5);
        assertFalse(produk.isStokAman());
        assertTrue(produk.isStokMenipis());
        assertFalse(produk.isStokHabis());
    }

    @Test
    @DisplayName("Test status stok - stok habis")
    void testStokHabis() {
        produk.setStok(0);
        produk.setStokMinimum(5);
        assertFalse(produk.isStokAman());
        assertFalse(produk.isStokMenipis());
        assertTrue(produk.isStokHabis());
    }

    @ParameterizedTest
    @DisplayName("Test kurangi stok dengan berbagai nilai")
    @CsvSource({
            "5, 5", // kurangi 5 dari 10, sisa 5
            "3, 7", // kurangi 3 dari 10, sisa 7
            "10, 0" // kurangi semua stok
    })
    void testKurangiStokValid(int jumlah, int expectedStok) {
        produk.kurangiStok(jumlah);
        assertEquals(expectedStok, produk.getStok());
    }

    @Test
    @DisplayName("Test kurangi stok - jumlah negatif")
    void testKurangiStokNegatif() {
        Exception exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    produk.kurangiStok(-5);
                });
        assertEquals("Jumlah harus positif", exception.getMessage());
    }

    @Test
    @DisplayName("Test kurangi stok - stok tidak mencukupi")
    void testKurangiStokTidakMencukupi() {
        Exception exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    produk.kurangiStok(15);
                });
        assertEquals("Stok tidak mencukupi", exception.getMessage());
    }

    @Test
    @DisplayName("Test tambah stok valid")
    void testTambahStokValid() {
        produk.tambahStok(5);
        assertEquals(15, produk.getStok());
    }

    @Test
    @DisplayName("Test tambah stok - jumlah negatif")
    void testTambahStokNegatif() {
        Exception exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    produk.tambahStok(-5);
                });
        assertEquals("Jumlah harus positif", exception.getMessage());
    }

    @ParameterizedTest
    @DisplayName("Test hitung total harga")
    @CsvSource({
            "1, 15000000",
            "2, 30000000",
            "5, 75000000"
    })
    void testHitungTotalHarga(int jumlah, double expectedTotal) {
        double total = produk.hitungTotalHarga(jumlah);
        assertEquals(expectedTotal, total, 0.001);
    }

    @Test
    @DisplayName("Test hitung total harga - jumlah negatif")
    void testHitungTotalHargaNegatif() {
        Exception exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    produk.hitungTotalHarga(-1);
                });
        assertEquals("Jumlah harus positif", exception.getMessage());
    }

    @Test
    @DisplayName("Test equals dan hashCode")
    void testEqualsAndHashCode() {
        Produk produk1 = new Produk("PROD001", "Laptop", "Elektronik", 1000000, 5, 2);
        Produk produk2 = new Produk("PROD001", "Laptop Baru", "Elektronik", 1200000, 3, 1);
        Produk produk3 = new Produk("PROD002", "Mouse", "Elektronik", 50000, 10, 5);
        assertEquals(produk1, produk2); // kode sama
        assertNotEquals(produk1, produk3); // kode berbeda
        assertEquals(produk1.hashCode(), produk2.hashCode());
    }

    @Test
    @DisplayName("Test boundary values untuk validasi stok")
    void testBoundaryValuesStok() {
        // Test stok = 0 (batas bawah)
        produk.setStok(0);
        assertTrue(produk.isStokHabis());

        // Test stok = 1 (tepat di atas batas bawah)
        produk.setStok(1);
        assertFalse(produk.isStokHabis());

        // Test stok = stokMinimum (boundary)
        produk.setStok(5);
        produk.setStokMinimum(5);
        assertTrue(produk.isStokMenipis());

        // Test stok = stokMinimum + 1 (tepat di atas boundary)
        produk.setStok(6);
        assertTrue(produk.isStokAman());
    }

    // ==================== TUGAS 2: TEST CASES TAMBAHAN ====================
    // ========== (UNTUK MENINGKATKAN COVERAGE YANG MASIH RENDAH) ==========

    // ✅ TAMBAHAN 1: Test untuk default constructor - SESUAI IMPLEMENTASI
    @Test
    @DisplayName("Test default constructor")
    void testDefaultConstructor() {
        Produk produkDefault = new Produk();

        assertNull(produkDefault.getKode());
        assertNull(produkDefault.getNama());
        assertNull(produkDefault.getKategori());
        assertEquals(0.0, produkDefault.getHarga(), 0.001);
        assertEquals(0, produkDefault.getStok());
        assertEquals(0, produkDefault.getStokMinimum());
        assertFalse(produkDefault.isAktif()); // ✅ SESUAI IMPLEMENTASI: default false
    }

    // ✅ TAMBAHAN 2: Test untuk semua setter methods
    @Test
    @DisplayName("Test semua setter methods")
    void testAllSetters() {
        Produk produk = new Produk();

        // Test semua setter
        produk.setKode("NEW001");
        produk.setNama("New Product");
        produk.setKategori("New Category");
        produk.setHarga(999000);
        produk.setStok(50);
        produk.setStokMinimum(10);
        produk.setAktif(true);

        // Verifikasi semua values
        assertEquals("NEW001", produk.getKode());
        assertEquals("New Product", produk.getNama());
        assertEquals("New Category", produk.getKategori());
        assertEquals(999000, produk.getHarga(), 0.001);
        assertEquals(50, produk.getStok());
        assertEquals(10, produk.getStokMinimum());
        assertTrue(produk.isAktif());

        // Test set aktif ke false
        produk.setAktif(false);
        assertFalse(produk.isAktif());
    }

    // ✅ TAMBAHAN 3: Test untuk toString method - SESUAI FORMAT
    @Test
    @DisplayName("Test toString method")
    void testToString() {
        String toStringResult = produk.toString();

        // Debug output untuk memastikan format
        System.out.println("DEBUG toString: " + toStringResult);

        // ✅ SESUAI IMPLEMENTASI: Format "Produk{...}" dengan field 'nama'
        assertTrue(toStringResult.startsWith("Produk{"));
        assertTrue(toStringResult.contains("kode='PROD001'"));
        assertTrue(toStringResult.contains("nama='Laptop Gaming'")); // ✅ 'nama' bukan 'name'
        assertTrue(toStringResult.contains("kategori='Elektronik'"));
        assertTrue(toStringResult.contains("harga=1.5E7") || toStringResult.contains("harga=15000000"));
        assertTrue(toStringResult.contains("stok=10"));
        assertTrue(toStringResult.contains("stokMinimum=5"));
        assertTrue(toStringResult.contains("aktif=true"));
    }

    // ✅ TAMBAHAN 4: Test edge cases untuk equals dan hashCode
    @Test
    @DisplayName("Test equals dengan null dan objek berbeda")
    void testEqualsEdgeCases() {
        Produk produk = new Produk("PROD001", "Test", "Category", 1000, 5, 2);

        // Test dengan null
        assertNotEquals(produk, null);

        // Test dengan objek class berbeda
        assertNotEquals(produk, "bukan produk");

        // Test reflexivity
        assertEquals(produk, produk);

        // Test dengan objek class berbeda
        assertNotEquals(produk, new Object());
    }

    // ✅ TAMBAHAN 5: Test untuk setter dengan boundary values
    @ParameterizedTest
    @DisplayName("Test setter dengan boundary values")
    @CsvSource({
            "0, 0",      // stok dan stokMinimum 0
            "1, 0",      // stok 1, stokMinimum 0
            "0, 1",      // stok 0, stokMinimum 1
            "1000, 100"  // nilai besar
    })
    void testSettersBoundaryValues(int stok, int stokMinimum) {
        Produk produk = new Produk();
        produk.setStok(stok);
        produk.setStokMinimum(stokMinimum);

        assertEquals(stok, produk.getStok());
        assertEquals(stokMinimum, produk.getStokMinimum());
    }

    // ✅ TAMBAHAN 6: Test untuk constructor edge cases
    @Test
    @DisplayName("Test constructor dengan nilai boundary")
    void testConstructorWithBoundaryValues() {
        // Test dengan stok 0 dan stokMinimum 0
        Produk produk1 = new Produk("PROD001", "Test", "Category", 1000, 0, 0);
        assertEquals(0, produk1.getStok());
        assertEquals(0, produk1.getStokMinimum());
        assertTrue(produk1.isAktif()); // ✅ Constructor dengan parameter: aktif = true

        // Test dengan nilai besar
        Produk produk2 = new Produk("PROD002", "Test", "Category", 9999999, 10000, 1000);
        assertEquals(10000, produk2.getStok());
        assertEquals(1000, produk2.getStokMinimum());
        assertTrue(produk2.isAktif()); // ✅ Constructor dengan parameter: aktif = true
    }

    // ✅ TAMBAHAN 7: Test untuk business logic dengan berbagai skenario aktif/non-aktif
    @Test
    @DisplayName("Test business logic dengan status aktif berbeda")
    void testBusinessLogicWithAktifStatus() {
        Produk produkAktif = new Produk("ACTIVE", "Active Product", "Category", 1000, 10, 5);
        Produk produkNonAktif = new Produk("INACTIVE", "Inactive Product", "Category", 1000, 10, 5);
        produkNonAktif.setAktif(false);

        // Business logic harus bekerja sama untuk produk aktif dan non-aktif
        assertTrue(produkAktif.isStokAman());
        assertTrue(produkNonAktif.isStokAman()); // logic stok tidak tergantung status aktif

        // Test kurangiStok untuk produk non-aktif
        produkNonAktif.kurangiStok(5);
        assertEquals(5, produkNonAktif.getStok());
    }

    // ✅ TAMBAHAN 8: Test untuk hashCode consistency
    @Test
    @DisplayName("Test hashCode consistency")
    void testHashCodeConsistency() {
        Produk produk1 = new Produk("PROD001", "Product One", "Category", 1000, 5, 2);
        Produk produk2 = new Produk("PROD001", "Product Two", "Different Category", 2000, 10, 3);

        // HashCode harus sama untuk objek dengan kode sama
        assertEquals(produk1.hashCode(), produk2.hashCode());

        // HashCode harus konsisten pada multiple calls
        int firstHash = produk1.hashCode();
        int secondHash = produk1.hashCode();
        assertEquals(firstHash, secondHash);
    }

    // ✅ TAMBAHAN 9: Test untuk setter dengan null values
    @Test
    @DisplayName("Test setter dengan null values")
    void testSettersWithNullValues() {
        Produk produk = new Produk();

        produk.setKode(null);
        assertNull(produk.getKode());

        produk.setNama(null);
        assertNull(produk.getNama());

        produk.setKategori(null);
        assertNull(produk.getKategori());
    }

    // ✅ TAMBAHAN 10: Comprehensive test untuk semua getter
    @Test
    @DisplayName("Test comprehensive untuk semua getter")
    void testAllGetters() {
        // Test getter dari constructor
        assertEquals("PROD001", produk.getKode());
        assertEquals("Laptop Gaming", produk.getNama());
        assertEquals("Elektronik", produk.getKategori());
        assertEquals(15000000, produk.getHarga(), 0.001);
        assertEquals(10, produk.getStok());
        assertEquals(5, produk.getStokMinimum());
        assertTrue(produk.isAktif()); // ✅ Constructor dengan parameter: aktif = true
    }

    // ✅ TAMBAHAN 11: Test constructor default vs parameterized
    @Test
    @DisplayName("Test perbedaan constructor default dan parameterized")
    void testConstructorComparison() {
        Produk defaultProduct = new Produk();
        Produk parameterizedProduct = new Produk("TEST", "Test", "Category", 1000, 5, 2);

        // Default constructor: semua null/0, aktif = false
        assertNull(defaultProduct.getKode());
        assertFalse(defaultProduct.isAktif());

        // Parameterized constructor: values di-set, aktif = true
        assertEquals("TEST", parameterizedProduct.getKode());
        assertTrue(parameterizedProduct.isAktif());
    }
}