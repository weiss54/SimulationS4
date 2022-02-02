public class EvenementOuverturePorteCabine extends Evenement {
    /* OPC: Ouverture Porte Cabine
       L'instant précis ou la porte termine de s'ouvrir.
    */
    
    public EvenementOuverturePorteCabine(long d) {
	super(d);
    }
    
    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
	buffer.append("OPC");
    }
    
    public void traiter(Immeuble immeuble, Echeancier echeancier) {
	Cabine cabine = immeuble.cabine;
	Etage étage = cabine.étage;
	assert ! cabine.porteOuverte;

	cabine.porteOuverte = true;
	int res = cabine.faireDescendrePassagers(immeuble, date);
	if(cabine.cabineEstVide() && !immeuble.passagerAuDessus(étage) && !immeuble.passagerEnDessous(étage))
		cabine.changerIntention('-');
	assert cabine.porteOuverte;
    }

}
