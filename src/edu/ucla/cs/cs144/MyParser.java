/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

import java.lang.Object;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    static BufferedWriter userWriter;
    static BufferedWriter itemWriter;
    static BufferedWriter categoryWriter;
    static BufferedWriter bidWriter;
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile)  throws IOException {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        
        Element[] elements = getElementsByTagNameNR(doc.getDocumentElement(), "Item");
        
        for(int i = 0; i < elements.length; i++)
        {
            parseUser(elements[i]);
            parseItem(elements[i]);
            parseCategory(elements[i]);
            parseBid(elements[i]);
        }
        
        
        /**************************************************************/
        
    }
    
    public static void parseUser(Element element) throws IOException
    {
        String userID = getElementByTagNameNR(element, "Seller").getAttribute("UserID");
        String country = getElementText(getElementByTagNameNR(element, "Country"));
        String address = getElementText(getElementByTagNameNR(element, "Location"));
        String sellerRating = getElementByTagNameNR(element, "Seller").getAttribute("Rating");
        parseWriter(userWriter, userID, country, address, sellerRating);
    }
    
    public static void parseItem(Element element) throws IOException
    {
        String itemID = element.getAttribute("ItemID");
        String userID = getElementByTagNameNR(element, "Seller").getAttribute("UserID");
        String itemName = getElementTextByTagNameNR(element, "Name");
        String firstBid = strip(getElementTextByTagNameNR(element, "First_Bid"));
        String buyPrice = strip(getElementTextByTagNameNR(element, "Buy_Price"));
        String currentHighestBid = strip(getElementTextByTagNameNR(element, "Currently"));
        String startTime = stringToTimestamp(getElementTextByTagNameNR(element, "Started"));
        String endTime = stringToTimestamp(getElementTextByTagNameNR(element, "Ends"));
        String numberOfBids = getElementTextByTagNameNR(element, "Number_of_Bids");
        String description = getElementTextByTagNameNR(element, "Description");
        if(description.length() > 4000)
        description = description.substring(0, 4000);
        parseWriter(itemWriter, itemID, userID, itemName, firstBid, buyPrice, currentHighestBid, startTime, endTime, numberOfBids, description);
    }
    
    public static String stringToTimestamp(String date) throws IOException
    {
        SimpleDateFormat format_in = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat format_out = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            Date parsedDate = format_in.parse(date);
            return "" + format_out.format(parsedDate);
        }
        catch(ParseException pe) {
            System.err.println("Parse error");
            return "Parse Error";
        }
    }
    
    public static void parseCategory(Element element) throws IOException
    {
        String itemID = element.getAttribute("ItemID");
        String category = "";
        Element[] categories = getElementsByTagNameNR(element, "Category");
        for(int i = 0; i < categories.length; i++)
        {
            category = getElementText(categories[i]);
            parseWriter(categoryWriter, itemID, category);
        }
    }

    public static void parseBid(Element element) throws IOException
    {
        String itemID = element.getAttribute("ItemID");
        String userID = "";
        String bidTime = "";
        String amount = "";
        Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(element, "Bids"), "Bid");
        
        for(int i = 0; i < bids.length; i++)
        {
                userID = getElementByTagNameNR(bids[i], "Bidder").getAttribute("UserID");
                bidTime = "" + stringToTimestamp(getElementTextByTagNameNR(bids[i], "Time"));
                amount = strip(getElementTextByTagNameNR(bids[i], "Amount"));
                parseWriter(bidWriter, userID, itemID, bidTime, amount);
        }
    }
    
    public static void parseWriter(BufferedWriter writer, String... args) throws IOException
    {
        String parseString = "";
        for(int i = 0; i < args.length - 1; i++)
        {
            parseString += args[i] + columnSeparator;
        }
        parseString += args[args.length - 1];
        
        writer.write(parseString);
        writer.newLine();
    }
       
    public static void main (String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        
        userWriter = new BufferedWriter(new FileWriter("rawUsers.dat", true));
        itemWriter = new BufferedWriter(new FileWriter("rawItems.dat", true));
        categoryWriter = new BufferedWriter(new FileWriter("rawCategories.dat", true));
        bidWriter = new BufferedWriter(new FileWriter("rawBids.dat", true));
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
        userWriter.close();
        itemWriter.close();
        categoryWriter.close();
        bidWriter.close();
    }
}
