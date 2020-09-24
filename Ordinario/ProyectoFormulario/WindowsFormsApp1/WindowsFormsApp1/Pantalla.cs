using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Threading;
using System.IO;
using System.Diagnostics;


namespace WindowsFormsApp1
{
    public partial class Pantalla : Form
    {
        public Pantalla()
        {
            InitializeComponent();
        }

        private void Abrir_Click(object sender, EventArgs e)
        {
            string command = "dir";


            command = "cmd /k wmic /output:software.txt product get Name, Version";


            CFirma();
            ExecuteCommand(command);
            FechayHora();
            Unir();
            eliminar();




            Process proceso = new Process();
            proceso.StartInfo.FileName = @"Inventario de Software.txt";
            proceso.Start();
            

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

     
        static void CFirma()
        {

            string path = @"Firma_IS.txt";


            // Create a file to write to.
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
            File.Delete(archivo1);
            File.Delete(archivo2);
            File.Delete(archivo3);
        }

        static void FechayHora()
        {
            string contenido = string.Format("Listado de software de el equipo    {0:dd/MM/yyyy HH:mm} ", DateTime.Now);
            string equipo = string.Format("MachineName: {0}", Environment.MachineName);


            string path = @"nombrearch.txt";


            // Create a file to write to.
            using (StreamWriter sw = File.CreateText(path))
            {
                sw.WriteLine(contenido);
                sw.WriteLine(equipo);
                sw.WriteLine("");


            }


        }
        private void label2_Click(object sender, EventArgs e)
        {

        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void Label13_Click(object sender, EventArgs e)
        {

        }

        private void Label14_Click(object sender, EventArgs e)
        {

        }

        private void Label10_Click(object sender, EventArgs e)
        {

        }

        private void Label8_Click(object sender, EventArgs e)
        {

        }

        private void Label12_Click(object sender, EventArgs e)
        {

        }

        private void Label11_Click(object sender, EventArgs e)
        {

        }

        private void Label2_Click_1(object sender, EventArgs e)
        {

        }

        private void Label9_Click(object sender, EventArgs e)
        {

        }

        private void Pantalla_Load(object sender, EventArgs e)
        {

        }

     

        private void Button1_Click_1(object sender, EventArgs e)
        {
            Convertir_PDF Cambio = new Convertir_PDF();
            Cambio.Show();
        }

      
    }
}
