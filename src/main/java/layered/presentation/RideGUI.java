package layered.presentation;

import javax.swing.*;

import java.awt.*;

import java.util.List;

import io.vertx.ext.web.client.WebClient;
import layered.business.ebike.EBike;

public class RideGUI extends JFrame {
    private final String id;
    private int credit;

    public RideGUI(String id, int credit, WebClient client, List<EBike> bikes) {
        this.id = id;
        this.credit = credit;

        var centralPanel = new VisualiserPanel(800, 500, bikes, id, credit);
	    add(centralPanel,BorderLayout.CENTER);

        setSize(720, 640);
        this.setVisible(true);
    }

    public static class VisualiserPanel extends JPanel {
        private long dx;
        private long dy;
        private final List<EBike> bikes;
        private final String id;
        private int credit;
        
        public VisualiserPanel(int w, int h, List<EBike> bikes, String id, int credit){
            setSize(w,h);
            dx = w/2 - 20;
            dy = h/2 - 20;
            this.bikes = bikes;
            this.id = id;
            this.credit = credit;
        }

        public void paint(Graphics g){
    		Graphics2D g2 = (Graphics2D) g;
    		
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    		          RenderingHints.VALUE_ANTIALIAS_ON);
    		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
    		          RenderingHints.VALUE_RENDER_QUALITY);
    		g2.clearRect(0,0,this.getWidth(),this.getHeight());
            
    		var it = bikes.iterator();
    		while (it.hasNext()) {
    			var b = it.next();
    			var p = b.getLocation();
    			int x0 = (int)(dx+p.getX());
		        int y0 = (int)(dy-p.getY());
		        g2.drawOval(x0,y0,20,20);
		        g2.drawString(b.getId(), x0, y0 + 35);
		        g2.drawString("(" + (int)p.getX() + "," + (int)p.getY() + ")", x0, y0+50);
    		} 
    		
            g2.drawRect(10, 20, 20, 20);
            g2.drawString(id + " - credit: " + credit, 35, 35);          
        }
        
        public void refresh(){
            repaint();
        }
    }
}
