using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO;
using System.Diagnostics;
using SautinSoft.Document;


namespace WindowsFormsApp1
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void Abrir_Click(object sender, EventArgs e)
        {
            string command = "dir";
            //string name = "dir";

            command = "cmd /k wmic /output:software.txt product get Name, Version";
            //name= "hostname >> name.txt";

            CFirma();
            ExecuteCommand(command);
            // Hostname(name);
            FechayHora();
            Unir();
            
            PDF_IS(); 
            eliminar();



            Process proceso = new Process();
            proceso.StartInfo.FileName = @"Inventario de Software.pdf";
            proceso.Start();
            return;

        }
        static void ExecuteCommand(string _Command)        {
            //Indicamos que deseamos inicializar el proceso cmd.exe junto a un comando de arranque. 
            //(/C, le indicamos al proceso cmd que deseamos que cuando termine la tarea asignada se cierre el proceso).
            //Para mas informacion consulte la ayuda de la consola con cmd.exe /? 
            System.Diagnostics.ProcessStartInfo procStartInfo = new System.Diagnostics.ProcessStartInfo("cmd", "/c " + _Command);
            // Indicamos que la salida del proceso se redireccione en un Stream
            procStartInfo.RedirectStandardOutput = true;
            procStartInfo.UseShellExecute = false;
            //Indica que el proceso no despliegue una pantalla negra (El proceso se ejecuta en background)
            procStartInfo.CreateNoWindow = false;
            //Inicializa el proceso
            System.Diagnostics.Process proc = new System.Diagnostics.Process();
            proc.StartInfo = procStartInfo;
            proc.Start();
            //Consigue la salida de la Consola(Stream) y devuelve una cadena de texto
            string result = proc.StandardOutput.ReadLine();
        }

        static void Hostname(string _Command)
        {
            
            System.Diagnostics.ProcessStartInfo procStartInfo = new System.Diagnostics.ProcessStartInfo("cmd", "/c " + _Command);
           
            procStartInfo.RedirectStandardOutput = true;
            procStartInfo.UseShellExecute = false;
            
            procStartInfo.CreateNoWindow = false;
            
            System.Diagnostics.Process proc = new System.Diagnostics.Process();
            proc.StartInfo = procStartInfo;
            proc.Start();
            
            string result = proc.StandardOutput.ReadLine();

        }
        static void CFirma()
        {

            string path = @"Firma_IS.txt";


            
            using (StreamWriter sw = File.CreateText(path))
            {
                sw.WriteLine("");
                sw.WriteLine("");
                sw.WriteLine("");
                sw.WriteLine(" ___________________");
                sw.WriteLine("Firma del encargado");

            }

        }

        static void Unir()
        {

            string contenidoPrimero = File.ReadAllText(@"Firma_IS.txt");
            string contenidoSegundo = File.ReadAllText(@"software.txt");
            string contenidoTercero = File.ReadAllText(@"nombrearch.txt");
            File.WriteAllText(@"Inventario de Software.txt", contenidoTercero + contenidoSegundo + contenidoPrimero);


        }


        static void eliminar()
        {
            string archivo1 = @"Firma_IS.txt";
            string archivo2 = @"software.txt";
            string archivo3 = @"nombrearch.txt";
            string archivo4 = @"Inventario de Software.txt";
            File.Delete(archivo1);
            File.Delete(archivo2);
            File.Delete(archivo3);
            File.Delete(archivo4);
        }

        static void FechayHora()
        {
            string contenido = string.Format("Listado de software de el equipo    {0:dd/MM/yyyy HH:mm} ", DateTime.Now);
            string equipo = string.Format("MachineName: {0}", Environment.MachineName); 

            string path = @"nombrearch.txt";


            using (StreamWriter sw = File.CreateText(path))
            {
                sw.WriteLine(contenido);
                sw.WriteLine(equipo);
                sw.WriteLine("");


            }


        }

        static void PDF_IS()
        {
            string inpFile = @"Inventario de Software.txt";
            string outFile = @"Inventario de Software.pdf";

            DocumentCore dc = DocumentCore.Load(inpFile);
            dc.Save(outFile);


        }

        private void label2_Click(object sender, EventArgs e)
        {

        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }
    }
}
