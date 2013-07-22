package tahrir.swingUI;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import tahrir.TrConstants;
import tahrir.TrNode;
import tahrir.io.crypto.TrCrypto;
import tahrir.io.net.microblogging.MicroblogParser;
import tahrir.io.net.microblogging.UserIdentity;
import tahrir.io.net.microblogging.containers.MicroblogsForViewing;
import tahrir.io.net.microblogging.microblogs.Microblog;
import tahrir.io.net.microblogging.microblogs.ParsedMicroblog;
import tahrir.tools.TrUtils;
import tahrir.ui.TrMainWindow;

import java.security.interfaces.RSAPrivateKey;
import java.util.SortedSet;

/**
 * User: ravisvi <ravitejasvi@gmail.com>
 * Date: 20/07/13
 */
public class GUIMain {

        public static void main(final String[] args) {
            try {
                final TrNode testNode = TrUtils.TestUtils.makeNode(9003, false, false, false, true, 0, 0);

                //UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");

                final TrMainWindow mainWindow = new TrMainWindow(testNode);
                mainWindow.getContent().revalidate();
                GUIMain.addTestInformationToNode(testNode);

            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        public static void addTestInformationToNode(final TrNode node) {
		/*
		  This is pretty silly: creating parsed microblogs and then, using their information, turn them into
		  their unparsed form and later insert them as if they were from broadcast.
		 */

            UserIdentity user1=new UserIdentity("user1", TrCrypto.createRsaKeyPair().a, Optional.<RSAPrivateKey>absent());
            UserIdentity user2=new UserIdentity("user2", TrCrypto.createRsaKeyPair().a, Optional.<RSAPrivateKey>absent());
            UserIdentity user3 = new UserIdentity("User 3", TrCrypto.createRsaKeyPair().a, Optional.of(TrCrypto.createRsaKeyPair().b));
            node.mbClasses.identityStore.addIdentityWithLabel(TrConstants.FOLLOWING, user1);
            node.mbClasses.identityStore.addIdentity(user2);
            node.mbClasses.identityStore.addIdentityWithLabel(TrConstants.OWN, user3);
            ParsedMicroblog fromRand = TrUtils.TestUtils.getParsedMicroblog();
            ParsedMicroblog fromUser1 = TrUtils.TestUtils.getParsedMicroblog(user1);
            ParsedMicroblog fromUser2 = TrUtils.TestUtils.getParsedMicroblog(user2, user1);
            ParsedMicroblog fromUser3 = TrUtils.TestUtils.getParsedMicroblog(user3);
            SortedSet<ParsedMicroblog> parsedMbs = Sets.newTreeSet(new MicroblogsForViewing.ParsedMicroblogTimeComparator());
            parsedMbs.add(fromRand);
            parsedMbs.add(fromUser1);
            parsedMbs.add(fromUser2);

            for (ParsedMicroblog parsedMicroblog : parsedMbs) {
                String xmlMessage = MicroblogParser.getXML(parsedMicroblog.getParsedParts());
                Microblog microblog = new Microblog(xmlMessage, parsedMicroblog.getMbData());
                node.mbClasses.incomingMbHandler.handleInsertion(microblog);
            }

            //checking to see if eventBus is working
            String xmlMessage = MicroblogParser.getXML(fromUser3.getParsedParts());
            Microblog microblog = new Microblog(xmlMessage, fromUser3.getMbData());
            node.mbClasses.incomingMbHandler.handleInsertion(microblog);

        }
}
