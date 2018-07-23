import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkDOT;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;
import org.graphstream.graph.Element;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class GraphFileManager {

        private static final List<String> GS_ATTR_EXCLUDES = Arrays.asList("x", "y", "z", "xy", "xyz",
                "ui.label", "ui.color", "ui.style", "ui.stylesheet", "ui.hide", "ui.quantity", "ui.antialias",
                "ui.screenshot");

        public static void main(String args[]) {
            Graph g = new SingleGraph("Tutorial 1");
            FileSource fs = new FileSourceDOT();

            fs.addSink(g);

            try {
                fs.readAll("out_example.dot");
                for(Node n : g.getNodeSet()) {
                    n.addAttribute("ui.label", n.getId());
                    for (Edge e : n.getEdgeSet()) {
                        e.addAttribute("ui.label", e.getAttribute("Weight").toString());
                    }
                }
                g.display();
                GraphFileManager.removeGraphStreamAttributes(g.getNodeSet());
                GraphFileManager.removeGraphStreamAttributes(g.getEdgeSet());
                FileSink fso = new FileSinkDOT(true);
                fso.writeAll(g, "out_example.dot");
            } catch( IOException e) {
                e.printStackTrace();
            } finally {
                fs.removeSink(g);
            }
        }

        public static void removeGraphStreamAttributes(Collection<? extends Element> set) {
            for(Element e : set) {
                Object[] attrs = e.getAttributeKeySet().toArray();
                for(int i = 0; i < attrs.length; i++) {
                    if(GS_ATTR_EXCLUDES.contains(attrs[i])) {
                        e.removeAttribute((String)attrs[i]);
                    }
                }
            }

        }
}
