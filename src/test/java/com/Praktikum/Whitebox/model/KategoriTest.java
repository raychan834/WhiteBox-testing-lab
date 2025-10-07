package com.Praktikum.Whitebox.model;

import com.praktikum.whitebox.model.Kategori;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test untuk Kelas Kategori")
class KategoriTest {

    private Kategori kategori;

    @BeforeEach
    void setUp() {
        kategori = new Kategori("K01", "Elektronik", "Perangkat elektronik rumah tangga");
    }

    /**
     * Deskripsi: Tes ini secara spesifik memanggil constructor `Kategori()` yang tidak memiliki argumen.
     * Tujuan: Untuk memastikan semua properti diinisialisasi dengan nilai default yang benar (`null` untuk String, `false` untuk `aktif`),
     * sekaligus menutupi constructor ini yang sebelumnya mungkin tidak teruji.
     */
    @Test
    @DisplayName("Konstruktor tanpa argumen harus membuat objek kosong")
    void testNoArgsConstructor() {
        Kategori kategoriKosong = new Kategori();
        assertNull(kategoriKosong.getKode(), "Kode harus null");
        assertNull(kategoriKosong.getNama(), "Nama harus null");
        assertFalse(kategoriKosong.isAktif(), "Status aktif harus false by default");
    }

    /**
     * Deskripsi: Tes ini memverifikasi bahwa constructor yang menerima parameter (`kode`, `nama`, `deskripsi`) dapat mengisi semua properti objek dengan benar.
     * Tujuan: Memastikan logika constructor berparameter berjalan sesuai harapan, termasuk memeriksa bahwa properti `aktif` secara otomatis diatur ke `true`,
     * dan semua metode getter mengembalikan nilai yang sesuai.
     */
    @Test
    @DisplayName("Konstruktor dengan argumen harus menginisialisasi properti dengan benar")
    void testParameterizedConstructor() {
        assertEquals("K01", kategori.getKode());
        assertEquals("Elektronik", kategori.getNama());
        assertEquals("Perangkat elektronik rumah tangga", kategori.getDeskripsi());
        assertTrue(kategori.isAktif(), "Status aktif harus otomatis true");
    }

    /**
     * Deskripsi: Sebuah tes komprehensif yang memanggil setiap metode setter pada objek `Kategori` (`setKode`, `setNama`, `setDeskripsi`, `setAktif`) untuk memberinya nilai baru.
     * Tujuan: Untuk memverifikasi bahwa semua metode setter berfungsi dengan benar dalam mengubah nilai properti objek.
     * Tes ini sangat penting untuk mencapai 100% Instruction Coverage pada metode-metode yang sebelumnya tidak teruji.
     */
    @Test
    @DisplayName("Setters harus berhasil mengubah nilai properti")
    void testSetters() {
        kategori.setKode("K02");
        assertEquals("K02", kategori.getKode());

        kategori.setNama("Perabotan");
        assertEquals("Perabotan", kategori.getNama());

        kategori.setDeskripsi("Perabotan kayu jati");
        assertEquals("Perabotan kayu jati", kategori.getDeskripsi());

        kategori.setAktif(false);
        assertFalse(kategori.isAktif());
    }

    /**
     * Deskripsi: Tes ini menguji metode `equals()` dan `hashCode()` dengan berbagai skenario, seperti membandingkan objek dengan dirinya sendiri,
     * dengan objek lain yang kodenya sama, dengan objek lain yang kodenya berbeda, dengan `null`, dan dengan objek dari kelas lain.
     * Tujuan: Untuk memastikan implementasi `equals()` dan `hashCode()` sesuai dengan "kontrak" atau aturan di Java.
     * Ini akan meningkatkan Branch Coverage secara signifikan karena menguji semua cabang `if` di dalam metode `equals`.
     */
    @Test
    @DisplayName("Metode equals() dan hashCode() harus bekerja sesuai kontrak")
    void testEqualsAndHashCode() {
        Kategori kategoriSama = new Kategori("K01", "Nama Berbeda", "Deskripsi Berbeda");
        Kategori kategoriBeda = new Kategori("K99", "Elektronik", "Perangkat elektronik rumah tangga");

        // 1. Refleksif
        assertEquals(kategori, kategori, "Objek harus sama dengan dirinya sendiri");
        // 2. Simetris
        assertEquals(kategori, kategoriSama, "Dua objek dengan kode yang sama harus dianggap equal");
        assertEquals(kategoriSama, kategori, "Equals harus simetris");
        // 3. Objek berbeda
        assertNotEquals(kategori, kategoriBeda, "Objek dengan kode berbeda tidak boleh equal");
        // 4. Perbandingan dengan null
        assertNotEquals(null, kategori, "Objek tidak boleh equal dengan null");
        // 5. Perbandingan dengan kelas lain
        assertNotEquals(kategori, new Object(), "Objek tidak boleh equal dengan tipe yang berbeda");
        // 6. Kontrak hashCode
        assertEquals(kategori.hashCode(), kategoriSama.hashCode(), "HashCode harus sama untuk objek yang equal");
    }

    /**
     * Deskripsi: Tes ini memanggil metode `toString()` dan memeriksa apakah string yang dihasilkan mengandung informasi-informasi kunci dari objek `Kategori`.
     * Tujuan: Memastikan metode `toString()` bekerja tanpa error dan menghasilkan output yang informatif, sehingga metode yang sebelumnya tidak teruji ini menjadi ter-cover.
     */
    @Test
    @DisplayName("Metode toString() harus menghasilkan representasi string yang benar")
    void testToString() {
        String output = kategori.toString();
        assertTrue(output.contains("kode='K01'"), "toString() harus mengandung kode");
        assertTrue(output.contains("nama='Elektronik'"), "toString() harus mengandung nama");
        assertTrue(output.contains("aktif=true"), "toString() harus mengandung status aktif");
    }
}