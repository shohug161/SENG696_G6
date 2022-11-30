package mkf.jade.sar.model;

/**
 * Teams that can login to and use the system
 * @author Rohit
 *
 */
public enum TeamType 
{
	noTeam,		// default value, used to indicate no one is logged in
	requestor, 
	zoneManager,
	asrc,	// Architecture & Security, Risk & Compliance
	aws,	// Applications & Web Services
	legal,	// Legal Services
	privacy,	// Privacy Office
	supplyChain,	// Supply chain management
	accountsPayable,	// Accounts payable
	deskside;	// Desk side technician
}
