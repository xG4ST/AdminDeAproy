# Instrucciones de uso

En la carpeta de cliente se encuentra el jar listo con su archivo de configuracion para ejecutarse.  

En la carpeta de Servidor se encuentra el jar listo para lanzarse y tambien su archivo de configuracion y en la carpeta de recursos se encuentra una java de tipo p12 lista para firmar el documento, la contraseña de esta llave es "password"

Se debera de ejecutar primero el servidor y despues el cliente, configurando en el cliente la ip del servidor, el puerto utilizado para la transferencia es el "13267".  

---

1. La contraseña de uso es "ProyectoDeAdministracionDeDesarrolloLosMartesV5" Sin las comillas.
1. El archivo config debe estar presente en todo momento junto a al jar ejecutable.  
1. La sintax del archivo config es la siguiente, comando para obtener programas, Comando para obtener informacion de, red, Destino donde se vaciara el pdf, Destino donde se vaciara el csv. cada uno en una linea su linea respectiva. El orden no es intercambiable y se debe de escribir solo el comando, no se pueden agregar lineas extras ni saltar lineas.
1. El programa requiere de la version de java13.  
1. El programa es exclusivo de uso en linux.  
1. El programa debe de ser lanzado desde consola utilizado OBLIGATORIAMENTE con privilegios "root" (super).  
1. El programa fue provado en la distribucion "fedora_31" y es probable que no funcione correctamente con otras distribuciones de linux, consulte la documentacion de su distribucion y modifique el archivo de configuracion (config.txt) para el "package manager" de su distribucion en particular

---

1. Para firmar el pdf se requiere del archivo "configFirma.txt" que debe de estar junto al jar ejecutable.  
1. La sintax de este archivo es similar a la del archivo "config.txt" con la diferencia de los valores que se le insertan,este recibe de valores: La ruta en la cual se imprimira el pdf firmado, El alias del dueño de la firma, El tipo o algoritmo de la firma, La ruta de la firma, La ruta de la imagen que se imprimira junto a la firma.  
1. Este programa fue programado para funcionar con la firma "PKCS12" o '.p12' y es posible que no funione correctamente con otro tipo de firmas.  
1. El programa debe de ser ejcutado con privilegios root para funciona.  

---

## instrucciones Del Cliente

1. Ejecute el programa habiendo cumplido con los requisitos anteriores, a diferencia de que al archivo de configuracio añadira la ip del servidor y el archivo de configuracion de firmas no es necesario.
1. Ingrese la contraseña para utilizar el programa. "ProyectoDeAdministracionDeDesarrolloLosMartesV5" sin comillas.
1. Listo, el pdf se enviara directo al servior.

## Instrucciones Del servidor

1. ejecute el programa cumpliendo con los requisitos anteriores, el archivo de configuracion no es necesario pero el de las firmas es obligatorio, con la sintax declarada en los primeros puntos.
1. El programa se quedara a la espera de conexiones, y al recibir una peticion pedira al usuario ingresar su contraseña de su firma digital, el usuario podra inspeccionar el archivo antes defirmarlo y puede decidir no firmarlo.
1. Una vez terminado el ciclo se generaran dos archivos, uno firmado y uno sin firma. **ATENCION** Debe de respaldar los archivos antes de que llegue la siguiente peticion ya que el programa los va a re escribir al llegar la nueva peticion.

## El programa debe de ser ejecutado exclusivamente en java13

Si el programa se ejecuto correctamente se generara un documento csv y un documento pdf en el directorio en el cual se encuentra el jar. o en la direccion especificada en el archivo de configuracion.

La marca de agua se debe a que una de las librerias necesarias para añadir la firma al pdf es de paga y la version de prueba genera esta marca de agua. si el programa va a ser utilizado para fines comerciales se sugiere comprar esta libreria.

## Este programa fue desarrollado por:

* Hector Antonio Gil Abad  1810976  IAS
* Carlos Alberto Heredia Beltran  1819988  IAS
