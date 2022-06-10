package Dormitory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;

public class Dormitory {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ArrayList<Room> szobak = readSzobakFromXml("src/main/resources/szobak.xml");
        System.out.println(szobak.size());
        System.out.println(szobak);

        int choice = -1;
        while (choice != 0) {
            switch (choice) {
                case 1 -> getRooms(szobak);
                case 2 -> addNewStudent(szobak);
                case 3 -> getRoomnumber(szobak);
                case 4 -> removeStudent(szobak);
                case 5 -> getnumberOfStudents(szobak);

            }
            System.out.println("1 - Szobák kilistázása\r\n2 - Új hallgató hozzáadása\r\n"
                    + "3 - Szobaszám kiírása\r\n4 -  Hallgató csökkentése\r\n5 - Szobában lakó hallgatók\r\n0 - Kilépés");
            try{
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < 0 || choice > 4) {
                    System.out.println("Nincs ilyen opció.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Nincs ilyen opció.");
                scanner.nextLine();
            }
        }

        saveRooms(szobak, "src/main/resources/szobak.xml");
    }

    public static ArrayList<Room> readSzobakFromXml(String filepath) {
        ArrayList<Room> szobak = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filepath);
            Element rootElement = document.getDocumentElement();
            NodeList childNodesList = rootElement.getChildNodes();
            int numberOfElementNodes = 0;
            Node node;
            for (int i = 0; i < childNodesList.getLength(); i++) {
                node = childNodesList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    numberOfElementNodes++;
                    NodeList childNodesOfUserTag = node.getChildNodes();
                    String szobaszam = "", hallgato = "";
                    for (int j = 0; j < childNodesOfUserTag.getLength(); j++) {
                        if (childNodesOfUserTag.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            switch (childNodesOfUserTag.item(j).getNodeName()) {
                                case "szobaszam" -> szobaszam = childNodesOfUserTag.item(j).getTextContent();
                                case "hallgato" -> hallgato = childNodesOfUserTag.item(j).getTextContent();
                            }
                        }
                    }
                    szobak.add(new Room(Integer.parseInt(szobaszam), Integer.parseInt(hallgato)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return szobak;
    }

    public static void saveRooms (ArrayList < Room > szobak, String filepath){
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element rootElement = document.createElement("szobak");
            document.appendChild(rootElement);

            for (Room szoba : szobak) {
                Element userElement = document.createElement("paciens");
                rootElement.appendChild(userElement);
                createChildElement(document, userElement, "szobaszam", String.valueOf(szoba.getSzobaszam()));
                createChildElement(document, userElement, "hallgato", String.valueOf(szoba.getHallgato()));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(filepath));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createChildElement (Document document, Element parent,
                                            String tagName, String value){
        Element element = document.createElement(tagName);
        element.setTextContent(value);
        parent.appendChild(element);
    }

    private static void getRooms (ArrayList < Room > szobak) {
        System.out.println(szobak);
    }

    private static int inputSzobaszam() {
        int szobaszam = 0;
        while (1000 > szobaszam || 99 < szobaszam) {
            try {
                System.out.print("Kérem adja meg a hallgató szobaszámát: ");
                szobaszam = scanner.nextInt();
                scanner.nextLine();
                if (szobaszam < 99 || szobaszam > 1000) {
                    System.out.println("A szobaszám kizárólag 3 számjegyű lehet!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Nem helyes a szobaszám.");
                scanner.nextLine();
            }
        }
        return szobaszam;
    }

    private static int inputHallgato() {
        int hallgato = 0;
        while (2 > hallgato || 0 < hallgato) {
            try {
                System.out.print("Kérem növelje a szobában tartózkodók számát eggyel: ");
                hallgato = scanner.nextInt();
                scanner.nextLine();
                if (hallgato < 6) {
                    System.out.println("A szobában maximum hatan lakhatnak!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Nem helyes a születési év.");
                scanner.nextLine();
            }
        }
        return hallgato;
    }

    private static void addNewStudent(ArrayList<Room> szobak) {
        int szobaszam = inputSzobaszam();
        int hallgato = inputHallgato();

        szobak.add(new Room(szobaszam, hallgato));
    }

    private static void removeStudent(ArrayList<Room> szobak) {
        System.out.print("Kérem adja meg a szobaszámot: ");
        int szobaszam = scanner.nextInt();
        for (Room szoba : szobak) {
            if (szoba.getSzobaszam() == szobaszam) {
                inputHallgato();
                System.out.println("Sikeresen törölt egy hallgatót a szobából.");
                return;
            }
        }
        System.out.println("Nincs ilyen szobaszám.");
    }

    private static void getRoomnumber(ArrayList<Room> szobak) {
        System.out.print("Kérem adja meg a szobaszámot: ");
        int szobaszam = scanner.nextInt();
        for (Room szoba : szobak) {
            if (szoba.getSzobaszam() == szobaszam) {
                System.out.println(szobaszam);
                return;
            }
        }
        System.out.println("Nincs ilyen szobaszám.");
    }

    private static void getnumberOfStudents(ArrayList<Room> szobak) {
        System.out.print("Kérem adja meg a szobaszámot: ");
        int szobaszam = scanner.nextInt();
        for (Room szoba : szobak) {
            if (szoba.getSzobaszam() == szobaszam){
                System.out.println(szobaszam);
                System.out.println(inputHallgato());
                return;
            }
        }
        System.out.println("Nincs ilyen szobaszám.");
    }
}
