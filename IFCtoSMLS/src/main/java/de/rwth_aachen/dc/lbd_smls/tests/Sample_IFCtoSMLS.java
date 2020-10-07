package de.rwth_aachen.dc.lbd_smls.tests;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.jena.rdf.model.Model;

import de.rwth_aachen.dc.lbd_smls.IFCtoLBDConverter_BIM4Ren;

public class Sample_IFCtoSMLS {
	
	
	static private void extractLBD_SMLS(File ifcFile, StringBuilder result_string) {
		IFCtoLBDConverter_BIM4Ren lbdconverter = new IFCtoLBDConverter_BIM4Ren();
		Model m = lbdconverter.convert(ifcFile.getAbsolutePath(), "https://dot.dc.rwth-aachen.de/IFCtoLBDset#");

		OutputStream ttl_output = new OutputStream() {
			private StringBuilder string = new StringBuilder();

			@Override
			public void write(int b) throws IOException {
				this.string.append((char) b);
			}

			public String toString() {
				return this.string.toString();
			}
		};
		//m.write(System.out, "TTL");
		m.write(ttl_output, "TTL");
		//RDFDataMgr.write(ttl_output, m, RDFFormat.JSONLD_COMPACT_PRETTY);
		result_string.append(ttl_output.toString());
	}
	
	
	public static void main(String[] args) {
		//String ifcFileName = "c:\\test\\bim4ren\\BIM4Ren_DUNANT_cleaned_IFC2x3.ifc";
		String ifcFileName = "C:\\test\\bim4ren\\BIM4Ren_DUNANT_cleaned_IFC2x3.ifc";
		File ifcFile = new File(ifcFileName);
		StringBuilder result_string=new StringBuilder();
		extractLBD_SMLS(ifcFile, result_string);
		System.out.println(result_string.toString());
	}
}
		
