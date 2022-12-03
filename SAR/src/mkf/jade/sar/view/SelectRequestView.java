package mkf.jade.sar.view;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import mkf.jade.sar.model.RequestInfoModel;

public class SelectRequestView extends JFrame {
	
/*******************************  Member Variable   ****************************************/
	
	/**
	 * Controls the UI
	 */
	private ViewController m_controller;
	
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 3831954889812709975L;

	/**
	 * List of requests that the user has tasks for
	 */
	private DefaultListModel<JPanel> m_requestsListModel;
	
	/**
	 * List used to track what 
	 */
	private ArrayList<Integer> m_requestIDRefList;
	
	/**
	 * JList of request info 
	 */
	private JList <JPanel> m_requestDisplayList;

	public SelectRequestView(ViewController controller)
	{
		m_controller = controller;
		
		m_requestsListModel = new DefaultListModel<JPanel>();
		m_requestDisplayList = new JList<JPanel>(m_requestsListModel);
		
		JLabel title = new JLabel("Choose Request");
		JButton chooseButton = new JButton("Choose");
		
		m_requestDisplayList.setFont(new Font("Serif", Font.PLAIN, 24));
		
		chooseButton.addActionListener(new ChooseButtonPressed(this));
		chooseButton.setFont(new Font("Serif", Font.PLAIN, 24));
		
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Serif", Font.PLAIN, 24));
		
		JPanel panel = new JPanel();
		
		panel.setLayout(new BorderLayout());
		panel.add(title, BorderLayout.NORTH);
		panel.add(m_requestDisplayList, BorderLayout.CENTER);
		panel.add(chooseButton, BorderLayout.SOUTH);

		panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
		setVisible(true);
	}
	
	/*******************************  Methods   ****************************************/

	/**
	 * Adds a name to the list
	 * @param name The name added to the list
	 */
	public void addRequest(RequestInfoModel requestInfo)
	{
		// TODO call when we receive a new task
		if(m_requestIDRefList.contains(requestInfo.requestID))
		{
			JPanel listEntry = new JPanel();
			listEntry.setLayout(new FlowLayout());
			
			JLabel requestIDLabel =  new JLabel(Integer.toString(requestInfo.requestID));
			JLabel softwareName =  new JLabel(requestInfo.softwareName);
			JLabel vendorName =  new JLabel("Vendor: " + requestInfo.vendorName);
			JLabel requestorName =  new JLabel("Requestor: " + requestInfo.requestorName);
			
			requestIDLabel.setHorizontalAlignment(SwingConstants.LEFT);
			softwareName.setHorizontalAlignment(SwingConstants.CENTER);
			vendorName.setHorizontalAlignment(SwingConstants.CENTER);
			requestorName.setHorizontalAlignment(SwingConstants.RIGHT);
			
			listEntry.add(requestIDLabel);
			listEntry.add(softwareName);
			listEntry.add(vendorName);
			listEntry.add(requestorName);
			
			m_requestIDRefList.add(requestInfo.requestID);
			m_requestsListModel.add(m_requestIDRefList.size() - 1, listEntry);
			
			m_requestDisplayList.setSelectedIndex(0);
			m_requestDisplayList.repaint();			
		}
	}
	
	/**
	 * Removes a request from the list
	 * @param name The request ID to remove
	 */
	public void removeRequest(int requestID)
	{
		// TODO call when a request is cancelled 
		// TODO call when all tasks are completed for a request for the logged in team 
		for(int i = m_requestIDRefList.size() - 1; i >= 0; i--)
		{
			if(m_requestIDRefList.get(i) == requestID)
			{
				m_requestIDRefList.remove(i);
				m_requestsListModel.remove(i);
			}
		}
		
		if(m_requestsListModel.size() > 0)
		{
			m_requestDisplayList.setSelectedIndex(0);
		}
		
		m_requestDisplayList.repaint();
	}
	
	public void clearList()
	{
		// TODO call on logoff
		m_requestsListModel.clear();
		m_requestIDRefList.clear();
	}
	
	/**
	 * Called when the user pressed the choose button
	 */
	public void chooseButtonPressed()
	{
		if(m_requestIDRefList.size() > 0)
		{
			int index = m_requestDisplayList.getSelectedIndex();
			//TODO m_controller.<REPLACE WITH METHOD TO CHOOSE A REQUEST>(m_requestIDRefList.get(index));
			
		}
	}
	
	/*******************************  Helper Class   ****************************************/
	
	/**
	 * Action listener for when the continue button is pressed
	 * @author Rohit
	 *
	 */
	private class ChooseButtonPressed implements ActionListener
	{
		public ChooseButtonPressed(SelectRequestView parent)
		{
			m_parent = parent;
		}

		private SelectRequestView m_parent;
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			m_parent.chooseButtonPressed();
		}
	}
}
