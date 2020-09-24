package servidor;

import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.qoppa.pdf.SignatureAppearance;
import com.qoppa.pdf.SigningInformation;
import com.qoppa.pdf.form.SignatureField;
import com.qoppa.pdfSecure.PDFSecure;


public class RecibirPdfYFirmar
{
	public final static int SOCKET_PORT = 13267;

	public final static int FILE_SIZE = 25022386;

	ConfigFirma Cf;

	public static void main(String[] args) throws Exception
	{
		RecibirPdfYFirmar R = new RecibirPdfYFirmar();
		R.Cf = R.new ConfigFirma();
		while (true)
		{
			R.Lanzador();
		}
	}

	public void Lanzador() throws Exception
	{
		int bytesRead;
		int current = 0;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		Socket sock = null;
		ServerSocket servsock = null;
		String Archivo = null;
		try
		{
			System.out.println("Esperando conexiones...");
			servsock = new ServerSocket(SOCKET_PORT);
			sock = servsock.accept();
			System.out.println("Recibiendo archivo desde..." + sock.getInetAddress());

			// receive file
			byte[] mybytearray = new byte[FILE_SIZE];
			InputStream is = sock.getInputStream();
			Archivo = "ListaFirmadaRemota.pdf";
			fos = new FileOutputStream(Archivo);
			bos = new BufferedOutputStream(fos);
			bytesRead = is.read(mybytearray, 0, mybytearray.length);
			current = bytesRead;
			do
			{
				bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
				if (bytesRead >= 0)
					current += bytesRead;
			} while (bytesRead > -1);
			bos.write(mybytearray, 0, current);
			bos.flush();
			System.out.println("Se descargo un archivo de tamaño " + current + " desde " + sock.getInetAddress());
		} finally
		{
			if (fos != null)
				fos.close();
			if (bos != null)
				bos.close();
			if (sock != null)
				sock.close();
			if (servsock != null)
				servsock.close();
		}
		Thread.sleep(1000);
		FirmarPdf(Archivo);
	}
	
	public void FirmarPdf(String archivo)
	{
		String clave = JOptionPane.showInputDialog("Inserte la contraseña de su firma digital");
		try
		{
			PDFSecure pdfDoc = new PDFSecure (archivo, null);
			 
			FileInputStream pkcs12Stream = new FileInputStream (Cf.Firma);
			KeyStore store = KeyStore.getInstance(Cf.TipoDeLlave);
			store.load(pkcs12Stream, clave.toCharArray());
			pkcs12Stream.close();
			 
			// Create signing information using the "Leila" alias
			SigningInformation signInfo = new SigningInformation(store, Cf.Alias, clave);
			 
	
			// Customize the signature appearance
			SignatureAppearance signAppear = signInfo.getSignatureAppearance();
	
			// Show an image instead of the signer's name on the left side of the signature field
			signAppear.setVisibleName(false);
			if(Cf.Imagen!=null)
			{
				signAppear.setImagePosition(SwingConstants.LEFT);
				signAppear.setImageFile(Cf.Imagen);
			}
			
			// Only show the signer's name and date on the right side of the signature field
			signAppear.setVisibleCommonName(false);
			signAppear.setVisibleOrgUnit(false);
			signAppear.setVisibleOrgName(false);
			signAppear.setVisibleLocal(false);
			signAppear.setVisibleState(false);
			signAppear.setVisibleCountry(false);
			signAppear.setVisibleEmail(false);
			 
			// Create signature field on the first page
			 
			SignatureField signField = pdfDoc.addSignatureField(0, "signature", new Rectangle2D.Double (20, 20, 300, 100));
			
			// Apply digital signature
			pdfDoc.signDocument(signField, signInfo);
			// Save the document
			pdfDoc.saveDocument (Cf.SalidaFirmada);
			System.out.println("El pdf fue firmado");
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("No se pudo firmar el pdf\nPosiblemente el archivo de configuracion este corrupto, mal escrito o la contraseña ingresada no fue valida\n");
		}
		System.out.println("Respalde el archivo, ya que al llegar la siguiente peticion el pdf sera sobre escrito");
	}
	
	public class ConfigFirma 
	{
		String SalidaFirmada;
		String Alias;
		String TipoDeLlave;
		String Firma;
		String Imagen;
		public ConfigFirma()
		{
			try
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("configFirma.txt")),"utf-8"));
				SalidaFirmada = br.readLine();
				Alias = br.readLine();
				TipoDeLlave = br.readLine();
				Firma = br.readLine();
				Imagen = br.readLine();
				br.close();
			}catch (Exception e) 
			{
				System.out.println("No se encontro archivo de configuracion de la firma");
				System.exit(0);
			}
			System.out.println("Se leyo archivo de configuracion de la firma");
		}
	}
}
