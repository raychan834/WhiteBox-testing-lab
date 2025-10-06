package com.Praktikum.Whitebox.util;

import com.praktikum.whitebox.model.Kategori;
import com.praktikum.whitebox.model.Produk;
import com.praktikum.whitebox.util.ValidationUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test untuk ValidationUtils")
public class ValidationUtilsTest {

    // ==================== TEST CASES YANG SUDAH ADA ====================

    //region Test untuk isValidKodeProduk
    @Test
    @DisplayName("Test isValidKodeProduk: Kode valid")
    void testIsValidKodeProduk_Valid() {
        assertTrue(ValidationUtils.isValidKodeProduk("PROD123"));
        assertTrue(ValidationUtils.isValidKodeProduk("A01"));
    }

    @ParameterizedTest(name = "Test isValidKodeProduk: Kode tidak valid - \"{0}\"")
    @ValueSource(strings = {"", "  ", "AB", "PROD123456789"}) // Input kosong, spasi, terlalu pendek, terlalu panjang
    @DisplayName("Test isValidKodeProduk: Berbagai kode tidak valid")
    void testIsValidKodeProduk_Invalid(String kode) {
        assertFalse(ValidationUtils.isValidKodeProduk(kode));
    }

    @Test
    @DisplayName("Test isValidKodeProduk: Kode null")
    void testIsValidKodeProduk_Null() {
        assertFalse(ValidationUtils.isValidKodeProduk(null));
    }
    //endregion

    //region Test untuk isValidNama
    @Test
    @DisplayName("Test isValidNama: Nama valid")
    void testIsValidNama_Valid() {
        assertTrue(ValidationUtils.isValidNama("Laptop Gaming Asus"));
    }

    @ParameterizedTest(name = "Test isValidNama: Nama tidak valid - \"{0}\"")
    @ValueSource(strings = {"", "  ", "LG"}) // Input kosong, spasi, terlalu pendek
    @DisplayName("Test isValidNama: Berbagai nama tidak valid")
    void testIsValidNama_Invalid(String nama) {
        assertFalse(ValidationUtils.isValidNama(nama));
    }

    @Test
    @DisplayName("Test isValidNama: Nama null")
    void testIsValidNama_Null() {
        assertFalse(ValidationUtils.isValidNama(null));
    }
    //endregion

    //region Test untuk isValidHarga, isValidStok, isValidKuantitas
    @Test
    @DisplayName("Test isValidHarga: Harga harus positif")
    void testIsValidHarga() {
        assertTrue(ValidationUtils.isValidHarga(1000));
        assertFalse(ValidationUtils.isValidHarga(0));
        assertFalse(ValidationUtils.isValidHarga(-1000));
    }

    @Test
    @DisplayName("Test isValidStok: Stok tidak boleh negatif")
    void testIsValidStok() {
        assertTrue(ValidationUtils.isValidStok(10));
        assertTrue(ValidationUtils.isValidStok(0));
        assertFalse(ValidationUtils.isValidStok(-1));
    }

    @Test
    @DisplayName("Test isValidKuantitas: Kuantitas harus positif")
    void testIsValidKuantitas() {
        assertTrue(ValidationUtils.isValidKuantitas(1));
        assertFalse(ValidationUtils.isValidKuantitas(0));
        assertFalse(ValidationUtils.isValidKuantitas(-1));
    }
    //endregion

    //region Test untuk isValidProduk
    @Test
    @DisplayName("Test isValidProduk: Produk valid")
    void testIsValidProduk_Valid() {
        Produk produk = new Produk("P01", "Mouse", "Elektronik", 150000, 10, 5);
        assertTrue(ValidationUtils.isValidProduk(produk));
    }

    @Test
    @DisplayName("Test isValidProduk: Produk null")
    void testIsValidProduk_Null() {
        assertFalse(ValidationUtils.isValidProduk(null));
    }

    @Test
    @DisplayName("Test isValidProduk: Properti tidak valid")
    void testIsValidProduk_InvalidProperties() {
        // Test dengan kode tidak valid
        Produk p1 = new Produk("P", "Mouse", "Elektronik", 150000, 10, 5);
        assertFalse(ValidationUtils.isValidProduk(p1));

        // Test dengan nama tidak valid
        Produk p2 = new Produk("P01", "M", "Elektronik", 150000, 10, 5);
        assertFalse(ValidationUtils.isValidProduk(p2));

        // Test dengan harga tidak valid
        Produk p3 = new Produk("P01", "Mouse", "Elektronik", -1, 10, 5);
        assertFalse(ValidationUtils.isValidProduk(p3));

        // Test dengan stok tidak valid
        Produk p4 = new Produk("P01", "Mouse", "Elektronik", 150000, -1, 5);
        assertFalse(ValidationUtils.isValidProduk(p4));
    }
    //endregion

    //region Test untuk isValidKategori
    @Test
    @DisplayName("Test isValidKategori: Kategori valid")
    void testIsValidKategori_Valid() {
        Kategori kategori = new Kategori("K01", "Elektronik", "Perangkat Elektronik");
        assertTrue(ValidationUtils.isValidKategori(kategori));
    }

    @Test
    @DisplayName("Test isValidKategori: Kategori null")
    void testIsValidKategori_Null() {
        assertFalse(ValidationUtils.isValidKategori(null));
    }

    @Test
    @DisplayName("Test isValidKategori: Properti tidak valid")
    void testIsValidKategori_InvalidProperties() {
        // Test dengan kode tidak valid
        Kategori k1 = new Kategori("K", "Elektronik", "");
        assertFalse(ValidationUtils.isValidKategori(k1));

        // Test dengan nama tidak valid
        Kategori k2 = new Kategori("K01", "E", "");
        assertFalse(ValidationUtils.isValidKategori(k2));
    }
    //endregion

    // ==================== TUGAS 2: TEST CASES TAMBAHAN ====================
    // ========== (UNTUK MENINGKATKAN COVERAGE YANG MASIH RENDAH) ==========

    // ✅ TAMBAHAN 1: Test untuk Constructor ValidationUtils (0% coverage)
    @Test
    @DisplayName("Test constructor ValidationUtils")
    void testConstructor() {
        // Test bahwa constructor bisa dipanggil (utility class)
        ValidationUtils utils = new ValidationUtils();
        assertNotNull(utils);
    }

    // ✅ TAMBAHAN 2: Test untuk isValidPersentase (0% coverage)
    @Test
    @DisplayName("Test isValidPersentase - boundary values")
    void testIsValidPersentase() {
        // Test boundary values
        assertTrue(ValidationUtils.isValidPersentase(0.0));    // batas bawah
        assertTrue(ValidationUtils.isValidPersentase(50.0));   // nilai tengah
        assertTrue(ValidationUtils.isValidPersentase(100.0));  // batas atas

        // Test invalid values
        assertFalse(ValidationUtils.isValidPersentase(-0.1));  // di bawah batas
        assertFalse(ValidationUtils.isValidPersentase(100.1)); // di atas batas
    }

    // ✅ TAMBAHAN 3: Parameterized Test untuk isValidPersentase
    @ParameterizedTest
    @DisplayName("Test isValidPersentase dengan berbagai nilai")
    @CsvSource({
            "0.0, true",      // batas bawah
            "1.0, true",      // tepat di atas batas bawah
            "99.9, true",     // tepat di bawah batas atas
            "100.0, true",    // batas atas
            "-0.1, false",    // di bawah batas bawah
            "100.1, false"    // di atas batas atas
    })
    void testIsValidPersentase_Parameterized(double persentase, boolean expected) {
        assertEquals(expected, ValidationUtils.isValidPersentase(persentase));
    }

    // ✅ TAMBAHAN 4: Test untuk isValidStokMinimum (83% coverage → 100%)
    @Test
    @DisplayName("Test isValidStokMinimum - comprehensive")
    void testIsValidStokMinimum_Comprehensive() {
        // Test boundary values
        assertTrue(ValidationUtils.isValidStokMinimum(0));   // batas bawah valid
        assertTrue(ValidationUtils.isValidStokMinimum(1));   // di atas batas bawah
        assertTrue(ValidationUtils.isValidStokMinimum(100)); // nilai besar

        // Test invalid
        assertFalse(ValidationUtils.isValidStokMinimum(-1)); // batas bawah invalid
    }

    // ✅ TAMBAHAN 5: Improve branch coverage untuk isValidProduk (77% → 100%)
    @Test
    @DisplayName("Test isValidProduk - edge cases untuk branch coverage")
    void testIsValidProduk_EdgeCases() {
        // Test dengan produk yang memiliki stok minimum = 0 (boundary)
        Produk produkStokMinNol = new Produk("PROD001", "Test", "Kategori", 1000, 10, 0);
        assertTrue(ValidationUtils.isValidProduk(produkStokMinNol));

        // Test dengan kategori yang panjangnya tepat 3 karakter (boundary)
        Produk produkKategoriMin = new Produk("PROD002", "Test", "ABC", 1000, 5, 2);
        assertTrue(ValidationUtils.isValidProduk(produkKategoriMin));

        // Test dengan stok minimum invalid
        Produk produkStokMinInvalid = new Produk("PROD003", "Test", "Kategori", 1000, 10, -1);
        assertFalse(ValidationUtils.isValidProduk(produkStokMinInvalid));
    }

    // ✅ TAMBAHAN 6: Improve branch coverage untuk isValidKategori (80% → 100%)
    @Test
    @DisplayName("Test isValidKategori - edge cases untuk branch coverage")
    void testIsValidKategori_EdgeCases() {
        // Test dengan deskripsi null (harus valid)
        Kategori kategoriDeskripsiNull = new Kategori("CAT001", "Test", null);
        assertTrue(ValidationUtils.isValidKategori(kategoriDeskripsiNull));

        // Test dengan deskripsi panjang tepat 500 karakter (boundary - valid)
        Kategori kategoriDeskripsiMax = new Kategori("CAT002", "Test", "A".repeat(500));
        assertTrue(ValidationUtils.isValidKategori(kategoriDeskripsiMax));

        // Test dengan deskripsi panjang 501 karakter (invalid)
        Kategori kategoriDeskripsiTooLong = new Kategori("CAT003", "Test", "A".repeat(501));
        assertFalse(ValidationUtils.isValidKategori(kategoriDeskripsiTooLong));
    }

    // ✅ TAMBAHAN 7: Improve branch coverage untuk isValidNama (87% → 100%)
    @Test
    @DisplayName("Test isValidNama - boundary values untuk branch coverage")
    void testIsValidNama_BoundaryValues() {
        // Test panjang tepat 3 karakter (batas bawah)
        assertTrue(ValidationUtils.isValidNama("ABC"));

        // Test panjang tepat 100 karakter (batas atas)
        assertTrue(ValidationUtils.isValidNama("A".repeat(100)));

        // Test panjang 2 karakter (invalid)
        assertFalse(ValidationUtils.isValidNama("AB"));

        // Test panjang 101 karakter (invalid)
        assertFalse(ValidationUtils.isValidNama("A".repeat(101)));

        // Test dengan whitespace trimming (harus tetap valid setelah trim)
        assertTrue(ValidationUtils.isValidNama("  Test Product  "));

        // Test dengan nama yang mengandung angka dan spasi (valid)
        assertTrue(ValidationUtils.isValidNama("Product 123 Test"));
    }

    // ✅ TAMBAHAN 8: Test untuk semua method yang belum ter-cover sepenuhnya
    @Test
    @DisplayName("Test comprehensive untuk semua validation methods")
    void testComprehensiveValidation() {
        // Test isValidStok dengan berbagai boundary values
        assertTrue(ValidationUtils.isValidStok(0));      // batas bawah
        assertTrue(ValidationUtils.isValidStok(1));      // di atas batas bawah
        assertTrue(ValidationUtils.isValidStok(1000));   // nilai besar
        assertFalse(ValidationUtils.isValidStok(-1));    // invalid

        // Test isValidKuantitas dengan boundary values
        assertTrue(ValidationUtils.isValidKuantitas(1));     // batas bawah
        assertTrue(ValidationUtils.isValidKuantitas(100));   // nilai besar
        assertFalse(ValidationUtils.isValidKuantitas(0));    // invalid
        assertFalse(ValidationUtils.isValidKuantitas(-1));   // invalid
    }
}