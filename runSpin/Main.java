/* Taken from here: https://github.com/DM2E/dm2e-spinval/blob/master/src/main/java/at/ac/onb/fue/spinapitest/DM2ESpinTest.java */ 

package runSpin;
import java.util.List;

import org.topbraid.spin.constraints.ConstraintViolation;
import org.topbraid.spin.constraints.SPINConstraints;
import org.topbraid.spin.inference.SPINInferences;
import org.topbraid.spin.system.SPINLabels;
import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.util.JenaUtil;
import org.topbraid.spin.constraints.SimplePropertyPath;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.shared.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


    @SuppressWarnings("deprecation")
public class Main 
{
    public static void main( String[] args )
    {
 org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.ERROR);
org.apache.log4j.BasicConfigurator.configure();

        // Initialize system functions and templates
        SPINModuleRegistry.get().init();

        // Load main file
        Model baseModel = ModelFactory.createDefaultModel(ReificationStyle.Minimal);

        try {
            InputStream owlStream = new FileInputStream("model.owl");
            baseModel.read(owlStream, null, "TTL");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
       

        Model dm2eInstData  = ModelFactory.createDefaultModel(ReificationStyle.Minimal);
        
        try {
            InputStream instStream = new FileInputStream("data.rdf");
            dm2eInstData.read(instStream, null, "TTL");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }        
        
        
        /*        try {
            OutputStream out = new FileOutputStream("testoutput.rdf");
            dm2eInstData.write(out);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        
        // Create OntModel with imports
        OntModel ontModel = JenaUtil.createOntologyModel(OntModelSpec.OWL_MEM,baseModel);
     
        // ADD THE DM2E INST TRIPLES TO the ontmodel
        ontModel.addSubModel(dm2eInstData);

        // Register locally defined functions
        SPINModuleRegistry.get().registerAll(ontModel, null);

        // Check all constraints
        List<ConstraintViolation> cvs = SPINConstraints.check(ontModel, null);
        System.out.println("Constraint violations:");
        for(ConstraintViolation cv : cvs) {
                System.out.println(cv.getRoot() + " " + cv.getMessage());
        }
    }
}
