package mkf.jade.sar.model;

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
