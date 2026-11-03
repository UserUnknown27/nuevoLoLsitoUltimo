package personajes.gui;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import personajes.base.Asesino;
import personajes.base.Campeon;
import personajes.base.Luchador;
import personajes.base.Mago;
import personajes.base.Soporte;
import personajes.base.Tanque;
import personajes.base.Tirador;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

public class Modelo {
    private ArrayList<Campeon> lista = new ArrayList<>();

    public ArrayList<Campeon> getLista() {
        return lista;
    }

    public void agregar(String tipo, String nombre, String region, LocalDate fecha, int mana, String descripcion) {
        lista.add(crear(tipo, nombre, region, fecha, mana, descripcion));
    }

    // hace el campeon segun el tipo
    private Campeon crear(String tipo, String nombre, String region, LocalDate fecha, int mana, String descripcion) {
        if (tipo.equals("Mago")) {
            return new Mago(nombre, region, fecha, mana, descripcion);
        } else if (tipo.equals("Luchador")) {
            return new Luchador(nombre, region, fecha, mana, descripcion);
        } else if (tipo.equals("Asesino")) {
            return new Asesino(nombre, region, fecha, mana, descripcion);
        } else if (tipo.equals("Tirador")) {
            return new Tirador(nombre, region, fecha, mana, descripcion);
        } else if (tipo.equals("Soporte")) {
            return new Soporte(nombre, region, fecha, mana, descripcion);
        } else {
            return new Tanque(nombre, region, fecha, mana, descripcion);
        }
    }

    // si no esta devuelve null
    public Campeon buscar(String nombre) {
        for (Campeon c : lista) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                return c;
            }
        }
        return null;
    }

    public void exportar(File archivo) throws Exception {
        DocumentBuilder constructor = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = constructor.newDocument();

        Element raiz = doc.createElement("campeones");
        doc.appendChild(raiz);

        for (Campeon c : lista) {
            // la etiqueta es el tipo
            Element e = doc.createElement(c.getTipo());
            raiz.appendChild(e);

            poner(doc, e, "nombre", c.getNombre());
            poner(doc, e, "region", c.getRegion());
            poner(doc, e, "fecha", c.getFecha().toString());
            poner(doc, e, "mana", String.valueOf(c.getMana()));
            poner(doc, e, "descripcion", c.getDescripcion());
        }

        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.transform(new DOMSource(doc), new StreamResult(archivo));
    }

    private void poner(Document doc, Element padre, String nombre, String valor) {
        Element e = doc.createElement(nombre);
        e.appendChild(doc.createTextNode(valor));
        padre.appendChild(e);
    }

    public void importar(File archivo) throws Exception {
        ArrayList<Campeon> nueva = new ArrayList<>();

        DocumentBuilder constructor = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = constructor.parse(archivo);

        NodeList hijos = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < hijos.getLength(); i++) {
            Node hijo = hijos.item(i);
            if (hijo instanceof Element) {
                Element e = (Element) hijo;
                Campeon c = crear(e.getTagName(),
                        leer(e, "nombre"),
                        leer(e, "region"),
                        LocalDate.parse(leer(e, "fecha")),
                        Integer.parseInt(leer(e, "mana")),
                        leer(e, "descripcion"));
                nueva.add(c);
            }
        }
        lista = nueva;
    }

    private String leer(Element padre, String nombre) {
        Node n = padre.getElementsByTagName(nombre).item(0);
        if (n == null) {
            return "";
        }
        return n.getTextContent();
    }
}
