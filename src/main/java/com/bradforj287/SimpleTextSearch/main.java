package com.bradforj287.SimpleTextSearch;

import com.google.common.base.Stopwatch;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

/**
 * Created by brad on 6/10/15.
 */
class main {

    public static void main(String args[]) throws Exception {

        File fXmlFile = new File("/Users/brad/Downloads/gaming.stackexchange.com/Posts.xml");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

        NodeList nList = doc.getElementsByTagName("row");

        List<Document> documentList = new ArrayList<>();
        Map<String, String> idToBody = new HashMap<>();

        for (int i = 0; i < nList.getLength(); i++) {

            Node n = nList.item(i);

            String body = n.getAttributes().getNamedItem("Body").toString();
            String id = n.getAttributes().getNamedItem("Id").toString();

            Document document = new Document(body, id);
            documentList.add(document);

            idToBody.put(id, body);
        }

        Stopwatch sw = Stopwatch.createUnstarted();
        sw.start();
        TextSearchIndex index = SearchIndexFactory.buildIndex(documentList);
        sw.stop();


        System.out.println("finished building index took " + sw.toString());
        System.out.println("num documents: " + index.numDocuments());
        System.out.println("num terms: " + index.termCount());

        Scanner scanner = new Scanner(System.in);

        String searchTerm = "";
        while (!searchTerm.equalsIgnoreCase("EXIT")) {
            System.out.print("Enter your search terms or type EXIT: ");

              searchTerm = scanner.nextLine();
            sw.reset();
            sw.start();
            SearchResultBatch batch = index.search(searchTerm, 3);
            sw.stop();

            System.out.println("printing results for term: " + searchTerm);
            for (SearchResult result : batch.getSearchResults()) {
                System.out.println("----------\n\n");
                System.out.println("score = " + result.getRelevanceScore());
                System.out.println(idToBody.get(result.getUniqueIdentifier().toString()));
            }

            System.out.println("finished searching took: " + sw.toString());
            System.out.println("num documents searched: " + batch.getStats().getDocumentsSearched());

        }

    }
}
