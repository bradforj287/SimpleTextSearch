package bradforj287.SimpleTextSearch;

import com.bradforj287.SimpleTextSearch.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brad on 6/7/15.
 */

public class SimpleTextSearchTest {


    @Test
    public void testSearchIndex() {

        String doc1 = "Shall I compare thee to a summer's day? \n" +
                "Thou art more lovely and more temperate:\n" +
                "Rough winds do shake the darling buds of May,\n" +
                "And summer's lease hath all too short a date: \n" +
                "Sometime too hot the eye of heaven shines,\n" +
                "And often is his gold complexion dimm'd; \n" +
                "And every fair from fair sometime declines,\n" +
                "By chance or nature's changing course untrimm'd;\n" +
                "But thy eternal summer shall not fade\n" +
                "Nor lose possession  possession of that fair thou owest;\n" +
                "Nor shall Death brag thou wander'st in his shade,\n" +
                "When in eternal lines to time thou growest: \n" +
                "So long as men can breathe or eyes can see,\n" +
                "So long lives this and this gives life to thee.\n";

        String doc2 = "Let me not to the marriage of true minds\n" +
                "Admit impediments. Love is not love\n" +
                "Which alters when it alteration finds,\n" +
                "Or bends with the remover to remove:\n" +
                "O no! it is an ever-fixed mark \n" +
                "That looks on tempests and is never shaken;\n" +
                "It is the star to every wandering bark,\n" +
                "Whose worth's unknown, possession although his height be taken.\n" +
                "Love's not Time's fool, though rosy lips and cheeks \n" +
                "Within his bending sickle's compass come: \n" +
                "Love alters not with his brief hours and weeks, \n" +
                "But bears it out even to the edge of doom.\n" +
                "If this be error and upon me proved,\n" +
                "I never writ, nor no man ever loved. ";

        String doc3 = "My mistress' eyes are nothing like the sun;\n" +
                "Coral is far more red than her lips' red;\n" +
                "If snow be white, why then her breasts are dun;\n" +
                "If hairs be wires, black wires grow on her head.\n" +
                "I have seen roses damask'd, red and white,\n" +
                "But no such roses see I possession in her cheeks; \n" +
                "And in some perfumes is there more delight\n" +
                "Than in the breath that from my mistress reeks.\n" +
                "I love to hear her speak, yet well I know\n" +
                "That music hath a far more pleasing sound;\n" +
                "I grant I never saw a goddess go;\n" +
                "My mistress, when she walks, treads on the ground:\n" +
                "And yet, by heaven, I think my love as rare\n" +
                "As any she belied with false compare. \n";

        String doc4 = "The expense of spirit in a waste of shame\n" +
                "Is lust in action; and till action, lust\n" +
                "Is perjured, murderous, bloody, full of blame,\n" +
                "Savage, extreme, rude, cruel, not to trust,\n" +
                "Enjoy'd no sooner but despised straight,\n" +
                "Past reason hunted, and no sooner had\n" +
                "Past reason hated, as a swallow'd bait\n" +
                "On purpose laid to make the taker mad;\n" +
                "Mad in pursuit and in possession possession so;\n" +
                "Had, having, and in quest to have, extreme;\n" +
                "A bliss in proof, and proved, a very woe;\n" +
                "Before, a joy proposed; behind, a dream.\n" +
                "All this the world well knows; yet none knows well\n" +
                "To shun the heaven that leads men to this hell.";

        List<Document> documents = new ArrayList<>();
        documents.add(new Document(doc1, new Integer(1)));
        documents.add(new Document(doc2, new Integer(2)));
        documents.add(new Document(doc3, new Integer(3)));
        documents.add(new Document(doc4, new Integer(4)));

        TextSearchIndex index = SearchIndexFactory.buildIndex(documents);

        String searchTerm = "Mad in pursuit and in possession so";

        SearchResultBatch batch = index.search(searchTerm, Integer.MAX_VALUE);
        List<SearchResult> results = batch.getSearchResults();

        // verify correct top result
        assert(results.get(0).getUniqueIdentifier().equals(4));

        boolean outOfOrder = false;
        for (int i = 0; i < results.size(); i++) {
            int next = i + 1;
            boolean hasNext = next < results.size();
            if (hasNext && ( results.get(next).getRelevanceScore() > results.get(i).getRelevanceScore() )) {
                outOfOrder = true;
            }
        }

        assert(outOfOrder == false);

        for (SearchResult result : results) {
            System.out.println(result.getUniqueIdentifier() + " " + result.getRelevanceScore());
        }
    }
}
