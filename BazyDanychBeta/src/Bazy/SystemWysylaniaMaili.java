
/*
 * Sprawdzanie czy w "wypozoczone" są ksiazki z data zwrotu <= dzisiejsza data - 14 dni
 * jesli tak, to do kazdego z userow ktorzy maja taka ksiazke, wysyla sie maila
 * 
 * reczne wywolywanie wyslania maila w przypadku gdy user zwroci zniszczona ksiazke albo ja zgubi
 */
package Bazy;

public interface SystemWysylaniaMaili {
	public void sprawdzStatusy();
	public void wyslijPrzypomnienie(String mail);
}
