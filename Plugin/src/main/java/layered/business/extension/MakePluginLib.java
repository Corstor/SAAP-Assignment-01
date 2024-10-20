package layered.business.extension;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class MakePluginLib {

	public static void main(String[] args) throws Exception {
		var jarFileName = "RechargeCredit.jar";
		var dir = "java";
		var p = Runtime.getRuntime().exec (new String[]{"jar", "cvf", jarFileName, dir}, new String[]{}, new File("target/classes"));		
		p.waitFor(10, TimeUnit.SECONDS);
		var dest = new File(jarFileName);
		dest.delete();
		new File("target/classes/"+jarFileName).renameTo(dest);
		System.out.println("done.");
	}	

}
