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

import mkf.jade.sar.model.InformationTypeHelper;
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
	private DefaultListModel<String> m_requestsListModel;
	
	private ArrayList<RequestInfoModel> requests;
	
	/**
	 * List used to track what 
	 */
	private ArrayList<Integer> m_requestIDRefList;
	
	/**
	 * JList of request info 
	 */
	private JList <String> m_requestDisplayList;
	
	private JPanel panel;

	public SelectRequestView(ViewController controller)
	{
		m_controller = controller;
		
		m_requestsListModel = new DefaultListModel<String>();
		m_requestDisplayList = new JList<String>(m_requestsListModel);	
		m_requestDisplayList.setFont(new Font("Serif", Font.PLAIN, 24));
		m_requestIDRefList = new ArrayList<Integer>();
		panel = new JPanel();
		requests = new ArrayList<RequestInfoModel>();

	}
	
	/*******************************  Methods   ****************************************/

	public void display() 
	{
		setSize(800, 800);
		JLabel title = new JLabel("Choose a Request");
		JButton chooseButton = new JButton("Choose");
		JButton cancelButton = new JButton("Cancel");
		
		JPanel buttonsPanel = new JPanel();

		chooseButton.addActionListener(new ChooseButtonPressed(this));
		chooseButton.setFont(new Font("Serif", Font.PLAIN, 24));
		
		cancelButton.addActionListener(new CancelButtonPressed());
		cancelButton.setFont(new Font("Serif", Font.PLAIN, 24));
		
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Serif", Font.PLAIN, 24));
				
		panel.setLayout(new BorderLayout());
		panel.add(title, BorderLayout.NORTH);
		panel.add(m_requestDisplayList, BorderLayout.CENTER);
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(chooseButton);
		panel.add(buttonsPanel, BorderLayout.SOUTH);

		panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
		add(panel);
		setVisible(true);
	}
	
	/**
	 * Adds a name to the list
	 * @param name The name added to the list
	 */
	public void addRequest(RequestInfoModel requestInfo)
	{
		if(!m_requestIDRefList.contains(requestInfo.requestID))
		{
			
			String request = Integer.toString(requestInfo.requestID) + ": " + requestInfo.softwareName + ", Vendor: " +
								requestInfo.vendorName + ", Requestor: " + requestInfo.requestorName;
			
			requests.add(requestInfo);

			m_requestIDRefList.add(requestInfo.requestID);
			m_requestsListModel.add(m_requestIDRefList.size() - 1, request);
			
			m_requestDisplayList.setSelectedIndex(0);
			m_requestDisplayList.revalidate();			
			m_requestDisplayList.repaint();	
			panel.revalidate();
			panel.repaint();
		}
	}
	
	/**
	 * Removes a request from the list
	 * @param name The request ID to remove
	 */
	public void removeRequest(int requestID)
	{
		// TODO call when all tasks are completed for a request for the logged in team 
		for(int i = m_requestIDRefList.size() - 1; i >= 0; i--)
		{
			if(m_requestIDRefList.get(i) == requestID)
			{
				m_requestIDRefList.remove(i);
				m_requestsListModel.remove(i);
				requests.remove(i);
			}
		}
		
		if(m_requestsListModel.size() > 0)
		{
			m_requestDisplayList.setSelectedIndex(0);
		}
		m_requestDisplayList.repaint();
		m_requestDisplayList.revalidate();
	}
	
	public void clearList()
	{
		m_requestsListModel.clear();
		m_requestIDRefList.clear();
		m_requestDisplayList.repaint();
		m_requestDisplayList.revalidate();
	}
	
	/**
	 * Called when the user pressed the choose button
	 */
	public void chooseButtonPressed()
	{
		if(m_requestIDRefList.size() > 0)
		{
			int index = m_requestDisplayList.getSelectedIndex();
			
			// get request information by the index and user name
			m_controller.chooseRequest(m_requestIDRefList.get(index), requests.get(index));
			dispose();
						
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
	
	private class CancelButtonPressed implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			
			m_controller.displayHomePage();
		}
		
	}
}
