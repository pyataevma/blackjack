package blackjack;

import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

class Blackjack {
	// maximal posible cantidad de cartas es 11: 4*2+3*3+4*1=21
	static int[] cartasJ = new int[11]; // cartas del Jugador 
	static int[] cartasC = new int[11]; // cartas del Crupie
	static int cantJ, cantC; // cantidad de cartas de Jugador y Crupier
	static double apuesta, total=0;
	
	public static boolean preguntaSiNo(String mensaje, String titulo, String imagen) {
		String s; 
		boolean error, res=false;
		s=(String)JOptionPane.showInputDialog(null,
				mensaje, titulo, JOptionPane.DEFAULT_OPTION,
				new ImageIcon(Blackjack.class.getResource(imagen)),null,null);
		do {
			error=false;
			switch (s.toUpperCase()) {
			case "SI":
				res=true;
				break;
			case "NO":
				res=false;
				break;
			default:
				error=true;
				s=(String)JOptionPane.showInputDialog(null,
						"¡Perdon! No te entendí.\nPor favor, escribí solo \"SI\" o \"NO\".\n" + mensaje, titulo, 
						JOptionPane.DEFAULT_OPTION,
						new ImageIcon(Blackjack.class.getResource(imagen)),null,null);
				break;
			}
		} while (error);
		return res;
	}
	
	public static int sacarCarta() {
		int carta;
		do {
			carta = (int)(Math.random()*52);
		} while (estaUsada(carta));
		return carta;
	}
	
	public static boolean estaUsada(int carta) {
		boolean usado = false;
		for (int i=0; i<cantJ; i++) {
			if (cartasJ[i] == carta) {
				usado=true;
			}
		}
		for (int i=0; i<cantC; i++) {
			if (cartasC[i] == carta) {
				usado=true;
			}
		}
		return usado; 
	}

	public static int hallarValor(int carta) {
		int valor;
		while (carta > 12) {
			carta-=13;
		}
		if (carta == 0) {
			valor = 11; // esto es As
		} else if (carta >= 10 && carta <= 12) {
			valor = 10;
		} else {
			valor=carta+1;
		}
		return valor;
	}
	
	public static String simboloDeCarta(int carta) {
		String s="";
		int palo=0;
		while (carta > 12) {
			carta-=13;
			palo++;
		}
		if (carta>=1 && carta <= 9) {
			s+=(carta+1);
		} else {
			switch (carta) {
			case 0:
				s="A"; // As
				break;
			case 10:
				s="J"; // Jota (Jack)
				break;
			case 11:
				s="Q"; // Reina (Queen)
				break;
			case 12:
				s="K"; // Rey (King)
				break;
			}
		}
		switch (palo) {
		case 0:
			s+="\u2660"; // espadas
			break;
		case 1:
			s+="\u2663"; // tréboles
			break;
		case 2:
			s+="\u2666"; // diamantes
			break;
		case 3:
			s+="\u2665"; // corazones
			break;
		}
		return s;
 	}
	
	public static String todasCartasJ() { // String con todas las cartas del Jugador
		String s="";
		for (int i=0; i<cantJ; i++) {
			s+=simboloDeCarta(cartasJ[i]) + ", ";
		}
		return s;
	}

	public static String todasCartasC() { // String con todas las cartas del Crupie
		String s="";
		for (int i=0; i<cantC; i++) {
			s+=simboloDeCarta(cartasC[i]) + ", ";
		}
		return s;
	}
	
	public static void repartirAlJugador() {
		cartasJ[cantJ]=sacarCarta();
		cantJ++;
		JOptionPane.showMessageDialog(null, "Ya tenés "+ todasCartasJ() + "\ntu suma es " + sumaJ()+".",
	    		   "Tu carta", JOptionPane.DEFAULT_OPTION, 
	    		   new ImageIcon(Blackjack.class.getResource("/imagenes/carta" + cartasJ[cantJ-1] + ".png")));
	}
	
	public static void repartirAlCrupie() {
		cartasC[cantC]=sacarCarta();
        cantC++;
        if (cantC==1) {
    		JOptionPane.showMessageDialog(null, "Crupier ya tiene "+todasCartasC()+"\nsu suma es " + sumaC(),
 	    		   "Carta del crupier", JOptionPane.DEFAULT_OPTION, 
 	    		   new ImageIcon(Blackjack.class.getResource("/imagenes/carta" + cartasC[cantC-1] + ".png")));
        } else if (cantC==2) {
    		JOptionPane.showMessageDialog(null, "La segunda carta del crupier \nestá tapada.",
  	    		   "Segunda carta del crupier", JOptionPane.DEFAULT_OPTION, 
 	    		   new ImageIcon(Blackjack.class.getResource("/imagenes/carta-tapada.png")));
        } else {
    		JOptionPane.showMessageDialog(null, "Crupier ya tiene "+todasCartasC()
    			+"\nsu suma es " + sumaC()+"."+reglaDeCrupier(),
  	    		"Carta del crupier", JOptionPane.DEFAULT_OPTION, 
  	    		 new ImageIcon(Blackjack.class.getResource("/imagenes/carta" + cartasC[cantC-1] + ".png")));
        }
	}

	public static String reglaDeCrupier() {
		String s;
		if (sumaC()>16) {
    		s="\nNo necesita más cartas.";
    	} else {
    		s="\nNecesita más cartas.";
    	}
        return s;
	}
	
	public static int sumaJ() {  // para Jugador
		int suma=0, as=0, valor;
		for (int i=0; i<cantJ; i++) {
			valor=hallarValor(cartasJ[i]);
			suma+=valor;
			if (valor==11) {
				as++; // cantidad de ases
			}
		}
		while (suma>21 && as>0) { //Si suma con As=11 es mas que 21, cada As=1.
			suma-=10;
			as--;
		}
		return suma;
 	}

	public static int sumaC() { // para Crupie
		int suma=0, as=0, valor;
		for (int i=0; i<cantC; i++) {
			valor=hallarValor(cartasC[i]);
			suma+=valor;
			if (valor==11) {
				as++; // cantidad de ases
			}
		}
		while (suma>21 && as>0) {
			suma-=10;
			as--;
		}
		return suma;
 	}
	
	public static void hacerApuesta() {
		double minApuesta=100, maxApuesta=10000; 
		apuesta=Double.parseDouble((String)JOptionPane.showInputDialog(null,
			"\nPodés hacer una apuesta entre "+ minApuesta + " y " + maxApuesta+ 
			".\n¿Cuál es tu apuesta?", 
			"Jugamos BlackJack", JOptionPane.DEFAULT_OPTION,
			new ImageIcon(Blackjack.class.getResource("/imagenes/monedas.gif")),null,null));
		while (apuesta < minApuesta || apuesta > maxApuesta) {
			apuesta=Double.parseDouble((String)JOptionPane.showInputDialog(null,
				"\nTu apuesta no está entre "+ minApuesta + " y " + maxApuesta+
				"\n¡Por favor, inténtelo de nuevo!" + ".\n¿Cuál es tu apuesta?", 
				"Jugamos BlackJack", JOptionPane.DEFAULT_OPTION,
				new ImageIcon(Blackjack.class.getResource("/imagenes/monedas.gif")),null,null));
		}
	}
	
	public static void turnoJ() { //turno del Jugador			
		boolean unaMas=false;
		do {
			unaMas=preguntaSiNo("Ya tenés "+ todasCartasJ() + "\nTu suma es "+ sumaJ()+
					"\n¿Querés recibir una carta más?", "Pedir o plantarse",
					"/imagenes/repartir2.gif");
			if (unaMas) {
				repartirAlJugador();
			}
		} while (unaMas && sumaJ()<21);
	}

	public static void turnoC() { //turno del Crupie
		JOptionPane.showMessageDialog(null, "La segunda carta del Crupier es "+simboloDeCarta(cartasC[cantC-1])
			+ ".\nCrupier ya tiene "+todasCartasC()+"\nsu suma es " + sumaC() +"." +reglaDeCrupier(),
			"Segunda carta del crupie", JOptionPane.DEFAULT_OPTION, 
			new ImageIcon(Blackjack.class.getResource("/imagenes/carta" + cartasC[cantC-1] + ".png")));
		while (sumaC() < 17) {
			repartirAlCrupie();
		}
	}
	
	public static void resumir(String resultado) {
		double premio=0;
		switch (resultado) {
		case "Empate":
			premio=0;
			JOptionPane.showMessageDialog(null, "Tenés "+ todasCartasJ() + "\ntu suma es " + sumaJ()+
				".\nCrupier tiene "+ todasCartasC() + "\nsu suma es " + sumaC()+ ".\n¡Empate!\nGuardaste tu apuesta.",
		    	"¡Empate!", JOptionPane.DEFAULT_OPTION,
		    	new ImageIcon(Blackjack.class.getResource("/imagenes/empate.gif")));
			break;
		case "BlackjackJugador":
			premio=1.5*apuesta;
			JOptionPane.showMessageDialog(null, "Tenés "+ todasCartasJ() + "\ntu suma es " + sumaJ()+
				".\n¡Blackjack!\nCrupier tiene "+ todasCartasC() + "\nsu suma es " + sumaC()+
				".\nGanaste 3:2.\nTu premio es " + premio + ".",
				"¡Felicidades!", JOptionPane.DEFAULT_OPTION,
				new ImageIcon(Blackjack.class.getResource("/imagenes/fireworks.gif")));
			break;
		case "BlackjackCrupier":
			premio=-apuesta;
			JOptionPane.showMessageDialog(null, "La segunda carta del Crupier es "
				+simboloDeCarta(cartasC[cantC-1]) + 
				".\nCrupier tiene "+todasCartasC()+"\nsu suma es " + sumaC()+
				".\n¡Blackjack!\nPerdiste tu apuesta.",
				"La proxima vez será mejor", JOptionPane.DEFAULT_OPTION, 
				new ImageIcon(Blackjack.class.getResource("/imagenes/carta" + cartasC[cantC-1] + ".png")));
			break;
		case "SePasoJugador":
			premio=-apuesta;
			JOptionPane.showMessageDialog(null, "Tenés "+ todasCartasJ() + "\ntu suma es " + sumaJ()+
				".\n¡Te pasaste!\nPerdiste tu apuesta.",
				"La proxima vez será mejor", JOptionPane.DEFAULT_OPTION,
				new ImageIcon(Blackjack.class.getResource("/imagenes/so-close.png")));
			break;
		case "SePasoCrupier":
			premio=apuesta;
			JOptionPane.showMessageDialog(null, "Crupier tiene "+ todasCartasC() + "\nsu suma es " + sumaC()+
				".\n¡Se pasó!\n¡Ganaste!\nTu premio es " + premio,
				"¡Felicidades!", JOptionPane.DEFAULT_OPTION,
				new ImageIcon(Blackjack.class.getResource("/imagenes/fireworks.gif")));
			break;
		case "GanoJugador":
			premio=apuesta;
			JOptionPane.showMessageDialog(null, "Tenés "+ todasCartasJ() + "\ntu suma es " + sumaJ()
				+".\nCrupier tiene "+ todasCartasC() + "\nsu suma es " + sumaC()+
				".\n¡Ganaste!\nTu premio es " + premio,	
				"¡Felicidaes!", JOptionPane.DEFAULT_OPTION,
				new ImageIcon(Blackjack.class.getResource("/imagenes/fireworks.gif")));
			break;
		case "GanoCrupier":
			premio=-apuesta;
			JOptionPane.showMessageDialog(null, "Tenés "+ todasCartasJ() + "\ntu suma es " + sumaJ()
				+".\nCrupier tiene "+ todasCartasC() + "\nsu suma es " + sumaC()+
				".\nPerdiste tu apuesta.",	
				"La proxima vez será mejor", JOptionPane.DEFAULT_OPTION,
				new ImageIcon(Blackjack.class.getResource("/imagenes/so-close.png")));
			break;
		}
		total+=premio;
	}
	
	public static void compararSumas() {
		if (sumaJ()==sumaC()) {
			resumir("Empate");
		} else if (sumaJ()>sumaC()) {
			resumir("GanoJugador");
		} else {
			resumir("GanoCrupier");
		}		
	}

	public static void jugarPartido() {
		cantJ=0; 
		cantC=0;
		hacerApuesta();
		repartirAlJugador();
		repartirAlJugador();
		repartirAlCrupie();
		repartirAlCrupie();
		if (sumaJ() == 21) { 
			if (sumaC() == 21) {				
				resumir("Empate");
			} else {
				resumir("BlackjackJugador");
			}
		} else if (sumaC() == 21) {
			  resumir("BlackjackCrupier"); 
		} else {
			turnoJ();
			if (sumaJ() > 21) {
				resumir("SePasoJugador");
			} else {
				turnoC();
				if (sumaC() > 21) {
					resumir("SePasoCrupier");
				} else {
					compararSumas();
				}
 			}
		}
	}

	public static void despedida(int cantPartidas) {
		String s="";
		if (total>0) {
			s="Ganaste " + total + ".\n¡Bien echo!";
		} else if (total<0){
			s="Perdiste "+(-total) + ".\n¡La próxima vez seguro que tendrás suerte!";
		} else {
			s="Guardaste tu dinero.\n¡La próxima vez será mejor!";
		}
		JOptionPane.showMessageDialog(null,	"Jugaste " + cantPartidas + " partidas.\n"+ 
			s + "\n¡Ven de nuevo!\n¡Te esperamos!",
			"¡Nos vemos!", JOptionPane.DEFAULT_OPTION,
			new ImageIcon(Blackjack.class.getResource("/imagenes/nos-vemos01.gif")));
	}
	
	public static void main(String[] args) {
		boolean unaMas;
		int cantPartidas=0;
		JOptionPane.showMessageDialog(null,	"",
			"Blackjack. Las reglas basicas", JOptionPane.DEFAULT_OPTION,
			new ImageIcon(Blackjack.class.getResource("/imagenes/reglas.png")));
		do {
			jugarPartido();
			cantPartidas++;
			unaMas=preguntaSiNo("¿Querés jugar una vez más?", "Blackjack", "/imagenes/blackjack.jpg");
		} while (unaMas);
		despedida(cantPartidas);
	}
}
