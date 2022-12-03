package mkf.jade.sar.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class ZoneManagerView extends JFrame {

	private ViewController m_viewController;
	
	
	public ZoneManagerView(ViewController vc)
	{
		m_viewController = vc;
	}
	
	// TODO
	// validate that is is information level 3 or 4
	// update information level if it needs to change
	// 
	
	
	
	class UpdateListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class ConfirmListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
