public class EvenementPassageCabinePalier extends Evenement {
    /* PCP: Passage Cabine Palier
       L'instant précis où la cabine passe juste en face d'un étage précis.
       Vous pouvez modifier cette classe comme vous voulez (ajouter/modifier des méthodes etc.).
    */
    
    private Etage étage;
    
    public EvenementPassageCabinePalier(long d, Etage e) {
	super(d);
	étage = e;
    }
    
    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
	buffer.append("PCP ");
	buffer.append(étage.numéro());
    }
    
    public void traiter(Immeuble immeuble, Echeancier echeancier) {
		Cabine cabine = immeuble.cabine;
		assert ! cabine.porteOuverte;
		assert étage.numéro() != cabine.étage.numéro();

		cabine.étage = étage;
		Etage etage = cabine.étage;

		if(!cabine.passagersVeulentDescendre()) {
			if(cabine.intention() == '^')
			etage = immeuble.étage(etage.numéro()+1);
			else
			etage = immeuble.étage(etage.numéro()-1);
			echeancier.ajouter(new EvenementPassageCabinePalier(date + Global.tempsPourBougerLaCabineDUnEtage, etage));
		} else {
			echeancier.ajouter(new EvenementOuverturePorteCabine(date+Global.tempsPourOuvrirOuFermerLesPortes));
		}
    }

}
