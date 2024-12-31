package banka2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

class düğüm {
	int hesapId;
	String adSoyad;
	String sube;
	double bakiye;
	düğüm sol, sag;
	
	public düğüm(int hesapId, String ad, String sube, double bakiye) {
		this.hesapId = hesapId;
		this.adSoyad = ad;
		this.sube = sube;
		this.bakiye = bakiye;
		this.sol = null;
		this.sag = null;
	}
}

class SistemBilgi {
	private düğüm kök;

	public SistemBilgi() {
		this.kök = null;
	}

	public void ekle(int hesapId, String ad, String sube, double bakiye) {
		kök = ekle(kök, hesapId, ad, sube, bakiye);
	}

	private düğüm ekle(düğüm dugum, int hesapId, String ad, String sube, double bakiye) {
		if (dugum == null) {
			return new düğüm(hesapId, ad, sube, bakiye);
		}
		if (hesapId < dugum.hesapId) {
			dugum.sol = ekle(dugum.sol, hesapId, ad, sube, bakiye);
		} else if (hesapId > dugum.hesapId) {
			dugum.sag = ekle(dugum.sag, hesapId, ad, sube, bakiye);
		}
		return dugum;
	}

	public düğüm ara(int hesapId) {
		return ara(kök, hesapId);
	}

	private düğüm ara(düğüm dugum, int hesapId) {
		if (dugum == null || dugum.hesapId == hesapId) {
			return dugum;
		}
		if (hesapId < dugum.hesapId) {
			return ara(dugum.sol, hesapId);
		}
		return ara(dugum.sag, hesapId);
	}

	public boolean yatir(int hesapId, double miktar) {
		düğüm hesap = ara(hesapId);
		if (hesap != null) {
			hesap.bakiye += miktar;
			System.out.println("Yatırma işlemi başarılı. Güncellenmiş hesap bilgileri:");
			System.out.println("HesapId: " + hesap.hesapId + ", Ad: " + hesap.adSoyad + ", Şube: " + hesap.sube
					+ ", Bakiye: " + hesap.bakiye);
			return true;
		}
		return false;
	}

	public String cek(int hesapId, double miktar) {
		düğüm hesap = ara(hesapId);
		if (hesap != null) {
			if (hesap.bakiye >= miktar) {
				hesap.bakiye -= miktar;
				System.out.println("Çekme işlemi başarılı. Güncellenmiş hesap bilgileri:");
				System.out.println("HesapId: " + hesap.hesapId + ", Ad: " + hesap.adSoyad + ", Şube: " + hesap.sube
						+ ", Bakiye: " + hesap.bakiye);
				return "";
			} else {
				return "Yetersiz bakiye";
			}
		}
		return "Hesap bulunamadı";
	}

	public void siraliListeleme() {
		siraliListelemeRec(kök);
	}

	private void siraliListelemeRec(düğüm dugum) {
		if (dugum != null) {
			siraliListelemeRec(dugum.sol);
			System.out.println("HesapId: " + dugum.hesapId + ", Ad: " + dugum.adSoyad + ", Şube: " + dugum.sube
					+ ", Bakiye: " + dugum.bakiye);
			siraliListelemeRec(dugum.sag);
		}
	}

	public void dosyadanYukle(String dosyaAdi) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(dosyaAdi))) {
			String satir;
			while ((satir = br.readLine()) != null) {
				String[] veri = satir.split(",");
				int hesapId = Integer.parseInt(veri[0]);
				String ad = veri[1];
				String sube = veri[2];
				double bakiye = Double.parseDouble(veri[3]);
				ekle(hesapId, ad, sube, bakiye);
			}
		}
	}
}

public class odev {
	public static void main(String[] args) throws IOException {
		SistemBilgi bankaSistemi = new SistemBilgi();

		String dosyaAdi = "kıllanıcı.txt";
		bankaSistemi.dosyadanYukle(dosyaAdi);

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out.println("\nBanka Sistemi Menüsü");
			System.out.println("1. Yeni bir hesap ekle");
			System.out.println("2. Bir hesabı ara");
			System.out.println("3. Para yatır");
			System.out.println("4. Para çek");
			System.out.println("5. Hesapları yazdır (Sıralı gezinti)");
			System.out.println("6. Çıkış");
			System.out.print("Seçiminizi girin: ");

			int secim = Integer.parseInt(br.readLine());

			switch (secim) {
			case 1:
				System.out.print("Hesap ID'si girin: ");
				int hesapId = Integer.parseInt(br.readLine());
				System.out.print("Müşteri Adı girin: ");
				String ad = br.readLine();
				System.out.print("Şube Adı girin: ");
				String sube = br.readLine();
				System.out.print("Başlangıç Bakiye girin: ");
				double bakiye = Double.parseDouble(br.readLine());
				bankaSistemi.ekle(hesapId, ad, sube, bakiye);
				System.out.println("Hesap başarıyla eklendi.");
				break;
			case 2:
				System.out.print("Aranacak Hesap ID'sini girin: ");

				hesapId = Integer.parseInt(br.readLine());
				düğüm hesap = bankaSistemi.ara(hesapId);
				if (hesap != null) {
					System.out.println("HesapId: " + hesap.hesapId + ", Ad: " + hesap.adSoyad + ", Şube: " + hesap.sube
							+ ", Bakiye: " + hesap.bakiye);
				} else {
					System.out.println("Hesap, ikili arama ağacında bulunamadı.");
				}
				break;
			case 3:
				System.out.print("Para yatırılacak Hesap ID'sini girin: ");
				hesapId = Integer.parseInt(br.readLine());
				System.out.print("Yatırılacak miktarı girin: ");
				double miktar = Double.parseDouble(br.readLine());
				if (bankaSistemi.yatir(hesapId, miktar)) {
					System.out.println();
				} else {
					System.out.println("Hesap bulunamadı.");
				}
				break;
			case 4:
				System.out.print("Para çekilecek Hesap ID'sini girin: ");
				hesapId = Integer.parseInt(br.readLine());
				System.out.print("Çekilecek miktarı girin: ");
				miktar = Double.parseDouble(br.readLine());
				String sonuc = bankaSistemi.cek(hesapId, miktar);
				System.out.println(sonuc);
				break;
			case 5:
				System.out.println("Hesaplar :");
				bankaSistemi.siraliListeleme();
				break;
			case 6:
				System.out.println("Programdan çıkılıyor.");
				return;
			default:
				System.out.println("Geçersiz seçenek. Lütfen tekrar deneyin.");
			}
		}
	}
}
