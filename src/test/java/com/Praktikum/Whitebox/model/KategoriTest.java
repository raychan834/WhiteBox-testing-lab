package com.Praktikum.Whitebox.model;

import com.praktikum.whitebox.model.Kategori;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// @DisplayName memberikan nama yang mudah dibaca untuk seluruh kelas tes ini di laporan pengujian.
@DisplayName("Test untuk Kelas Kategori")
class KategoriTest {

    // Mendeklarasikan variabel untuk objek Kategori yang akan kita uji.
    private Kategori kategori;

    // Anotasi @BeforeEach dari JUnit 5.
    // Metode ini akan dijalankan SECARA OTOMATIS sebelum SETIAP metode tes di kelas ini.
    // Tujuannya adalah untuk memastikan setiap tes dimulai dengan objek yang "bersih" dan sama.
    @BeforeEach
    void setUp() {
        // Membuat instance baru dari Kategori.
        // Ini memastikan tes tidak saling mempengaruhi.
        kategori = new Kategori("K01", "Elektronik", "Perangkat elektronik rumah tangga");
    }

    // Tes ini khusus untuk memeriksa konstruktor default (tanpa argumen).
    @Test
    @DisplayName("Konstruktor tanpa argumen harus membuat objek kosong")
    void testNoArgsConstructor() {
        // Membuat objek menggunakan `new Kategori()`
        Kategori kategoriKosong = new Kategori();

        // Memastikan bahwa properti yang berupa objek (String) bernilai null.
        assertNull(kategoriKosong.getKode(), "Kode harus null");
        assertNull(kategoriKosong.getNama(), "Nama harus null");

        // Memastikan bahwa properti boolean `aktif` bernilai false secara default.
        assertFalse(kategoriKosong.isAktif(), "Status aktif harus false by default");
    }

    // Tes ini memverifikasi bahwa konstruktor yang menerima parameter
    // mengisi semua properti objek dengan benar.
    @Test
    @DisplayName("Konstruktor dengan argumen harus menginisialisasi properti dengan benar")
    void testParameterizedConstructor() {
        // assertEquals membandingkan nilai yang diharapkan (argumen 1) dengan nilai aktual (argumen 2).
        assertEquals("K01", kategori.getKode());
        assertEquals("Elektronik", kategori.getNama());
        assertEquals("Perangkat elektronik rumah tangga", kategori.getDeskripsi());

        // assertTrue memeriksa apakah nilai `aktif` adalah true sesuai logika konstruktor.
        assertTrue(kategori.isAktif(), "Status aktif harus otomatis true");
    }

    // Tes ini bertujuan untuk memastikan semua metode setter berfungsi sebagaimana mestinya.
    @Test
    @DisplayName("Setters harus berhasil mengubah nilai properti")
    void testSetters() {
        // Menguji setter untuk 'kode'
        kategori.setKode("K02");
        assertEquals("K02", kategori.getKode());

        // Menguji setter untuk 'nama'
        kategori.setNama("Perabotan");
        assertEquals("Perabotan", kategori.getNama());

        // Menguji setter untuk 'deskripsi'
        kategori.setDeskripsi("Perabotan kayu jati");
        assertEquals("Perabotan kayu jati", kategori.getDeskripsi());

        // Menguji setter untuk 'aktif'
        kategori.setAktif(false);
        assertFalse(kategori.isAktif());
    }

    // Ini adalah tes yang paling kompleks, memverifikasi logika `equals` dan `hashCode`
    // agar sesuai dengan "kontrak" atau aturan di Java.
    @Test
    @DisplayName("Metode equals() dan hashCode() harus bekerja sesuai kontrak")
    void testEqualsAndHashCode() {
        // SETUP: Membuat objek pembanding
        // Objek ini seharusnya dianggap SAMA karena `kode`-nya sama ("K01").
        Kategori kategoriSama = new Kategori("K01", "Nama Berbeda", "Deskripsi Berbeda");
        // Objek ini seharusnya dianggap BEDA karena `kode`-nya berbeda ("K99").
        Kategori kategoriBeda = new Kategori("K99", "Elektronik", "Perangkat elektronik rumah tangga");

        // ASSERT: Melakukan berbagai macam pengujian
        // 1. Refleksif: sebuah objek harus selalu sama dengan dirinya sendiri.
        assertEquals(kategori, kategori, "Objek harus sama dengan dirinya sendiri");

        // 2. Simetris: jika A sama dengan B, maka B harus sama dengan A.
        assertEquals(kategori, kategoriSama, "Dua objek dengan kode yang sama harus dianggap equal");
        assertEquals(kategoriSama, kategori, "Equals harus simetris");

        // 3. Memastikan objek dengan `kode` berbeda tidak dianggap sama.
        assertNotEquals(kategori, kategoriBeda, "Objek dengan kode berbeda tidak boleh equal");

        // 4. Memastikan objek tidak sama dengan `null`.
        assertNotEquals(null, kategori, "Objek tidak boleh equal dengan null");

        // 5. Memastikan objek tidak sama dengan objek dari kelas yang berbeda.
        assertNotEquals(kategori, new Object(), "Objek tidak boleh equal dengan tipe yang berbeda");

        // 6. Kontrak hashCode: jika dua objek `equals`, maka `hashCode`-nya WAJIB sama.
        assertEquals(kategori.hashCode(), kategoriSama.hashCode(), "HashCode harus sama untuk objek yang equal");
    }

    // Tes ini memastikan metode `toString()` menghasilkan output yang informatif.
    @Test
    @DisplayName("Metode toString() harus menghasilkan representasi string yang benar")
    void testToString() {
        // Panggil metode toString() dan simpan hasilnya di variabel.
        String output = kategori.toString();

        // Kita tidak perlu memeriksa keseluruhan string secara persis, karena itu terlalu kaku.
        // Cukup pastikan string output mengandung informasi-informasi penting.
        assertTrue(output.contains("kode='K01'"), "toString() harus mengandung kode");
        assertTrue(output.contains("nama='Elektronik'"), "toString() harus mengandung nama");
        assertTrue(output.contains("aktif=true"), "toString() harus mengandung status aktif");
    }
}