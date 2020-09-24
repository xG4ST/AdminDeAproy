package cliente;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;


import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileReader;
import au.com.bytecode.opencsv.CSVReader;
import prot.MyTablaPDF;

public class ClienteEnvia
{
	Config C;
	String Pass;
	
	public static void main(String[] args) throws DocumentException, IOException
	{
		ClienteEnvia G = new ClienteEnvia();
		G.Pass = MyTablaPDF.VerificarL(null);
		G.C = G.new Config();
		G.ObtenerProgramas();
		G.Pdf();
		G.EnviarAServidor();
		System.out.println("Fin de la ejecucion");
	}

	public void ObtenerProgramas()
	{
		String s;
		Process p;
		ArrayList<String> Programas = new ArrayList<String>();
		Programas.add("Programa,Version,Fuente");
		try
		{
			p = Runtime.getRuntime().exec(C.ComandoProgramas);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((s = br.readLine()) != null)
			{
				String str = s;
				while (str.contains("  "))
				{
					str = str.replaceAll("  ", " ");
				}
				str = str.replaceFirst(" ", ",");
				str = str.replaceFirst(" ", ",");
				str = str.replaceAll("\n", "");
				Programas.add(str);
			}
			p.waitFor();
			p.destroy();

		} catch (Exception e)
		{
			System.out.println("Error al obtener la lista de archivos");
			System.exit(0);
		}
		Programas.remove(1);
		Programas.remove(1);
		try
		{
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(C.SalidaCsv), "utf-8"));
			for (String st : Programas)
			{
				writer.append(st + "\n");
			}
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Error al guardar el archivo");
			System.exit(0);
		}
		System.out.println("Lista de programas generada con exito");
	}

	public void Pdf() throws DocumentException, IOException
	{

		/* Step -1 : Read input CSV file in Java */
		CSVReader reader = new CSVReader(new FileReader(C.SalidaCsv));
		/* Variables to loop through the CSV File */
		String[] nextLine; /* for every line in the file */
//		int lnNum = 0; /* line number */
		/* Step-2: Initialize PDF documents - logical objects */
		Document my_pdf_data = new Document();
		PdfWriter.getInstance(my_pdf_data, new FileOutputStream(C.SalidaPdf));
		my_pdf_data.open();

		MyTablaPDF TablaDeDatos = new MyTablaPDF(4,Pass);
		TablaDeDatos.addCell(new PdfPCell(new Phrase("Nombre")));
		TablaDeDatos.addCell(new PdfPCell(new Phrase("Matricula")));
		TablaDeDatos.addCell(new PdfPCell(new Phrase("Carrera")));
		TablaDeDatos.addCell(new PdfPCell(new Phrase("Hora")));

		TablaDeDatos.addCell(new PdfPCell(new Phrase("Hector Antonio Gil Abad")));
		TablaDeDatos.addCell(new PdfPCell(new Phrase("1810976")));
		TablaDeDatos.addCell(new PdfPCell(new Phrase("IAS")));
		TablaDeDatos.addCell(new PdfPCell(new Phrase("V4-5-6 MARTES")));

		TablaDeDatos.addCell(new PdfPCell(new Phrase("Carlos Alberto Heredia Beltran")));
		TablaDeDatos.addCell(new PdfPCell(new Phrase("1819988")));
		TablaDeDatos.addCell(new PdfPCell(new Phrase("IAS")));
		TablaDeDatos.addCell(new PdfPCell(new Phrase("V4-5-6 MARTES")));

		MyTablaPDF my_first_table = new MyTablaPDF(3,Pass);
		PdfPCell table_cell;
		/* Step -3: Loop through CSV file and populate data to PDF table */
		while ((nextLine = reader.readNext()) != null)
		{
//			lnNum++;
			table_cell = new PdfPCell(new Phrase(nextLine[0]));
			my_first_table.addCell(table_cell);
			table_cell = new PdfPCell(new Phrase(nextLine[1]));
			my_first_table.addCell(table_cell);
			table_cell = new PdfPCell(new Phrase(nextLine[2]));
			my_first_table.addCell(table_cell);
		}
		/* Step -4: Attach table to PDF and close the document */
		my_pdf_data.addTitle("Programas instalados en la computadora");
		my_pdf_data.add(new Phrase("\n\n\n\n\n\n\n"));
		my_pdf_data.add(new Phrase("Informacion de red de la pc\n"+Red()));
		my_pdf_data.add(new Phrase("\nDesarrolladores de este software"));
		my_pdf_data.add(TablaDeDatos);
		my_pdf_data.add(new Phrase("\n\n"));
		my_pdf_data.setHeader(new HeaderFooter(new Phrase("Auditores: Hector Antonio Gil Abad  1810976  IAS\nCarlos Alberto Heredia Beltran  1819988  IAS\nPagina: "), true));
		my_pdf_data.add(my_first_table);
		my_pdf_data.close();
		System.out.println("Pdf generado con exito");
	}

	public String Red()
	{
		String str = null;
		try
		{
			Process p = Runtime.getRuntime().exec(C.ComandoRed);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s;

			while ((s = br.readLine()) != null)
			{
				str += s + "\n";
			}
			p.waitFor();
			p.destroy();

		} catch (Exception e)
		{
			System.out.println("Error al obtener informacion de red");
			System.exit(0);
		}
		return str;
	}
	public boolean EnviarAServidor() throws IOException
	{
		int SOCKET_PORT = 13267;
		boolean R = false;
		FileInputStream fis = null;
	    BufferedInputStream bis = null;
	    OutputStream os = null;
	    Socket sock = null;
	    try 
	    {
	      while (true) 
	      {
	        System.out.println("Esperando a que se acepte la conexion...");
	        try
	        {
	          sock = new Socket(C.IpServidor, SOCKET_PORT);
	          System.out.println("Conexion aceptada : " + sock);
	          
	          // send file
	          File myFile = new File (C.SalidaPdf);
	          byte [] mybytearray  = new byte [(int)myFile.length()];
	          fis = new FileInputStream(myFile);
	          bis = new BufferedInputStream(fis);
	          
	          bis.read(mybytearray,0,mybytearray.length);
	          os = sock.getOutputStream();
	          System.out.println("Enviando " + C.SalidaPdf + "(" + mybytearray.length + " bytes) al servidor");
	          os.write(mybytearray,0,mybytearray.length);
	          os.flush();
	          System.out.println("Listo.");
	          break;
	        }
	        finally 
	        {
	          if (bis != null) bis.close();
	          if (os != null) os.close();
	          if (sock!=null) sock.close();
	        }
	      }
	    }
	    finally 
	    {
	    }
		return R;
	}
	public class Config 
	{
		String ComandoProgramas;
		String ComandoRed;
		String SalidaPdf;
		String SalidaCsv;
		String IpServidor;
		
		public Config()
		{
			try
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("config.txt")),"utf-8"));
				ComandoProgramas = br.readLine();
				ComandoRed = br.readLine();
				SalidaPdf = br.readLine();
				SalidaCsv = br.readLine();
				IpServidor = br.readLine();
				br.close();
			}catch (Exception e) 
			{
				System.out.println("No se encontro archivo de configuracion");
				System.exit(0);
			}
			System.out.println("Archivo de configuracion leido con exito");
		}
		
		
	}
}
