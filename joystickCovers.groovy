import eu.mihosoft.vrl.v3d.parametrics.*;
import java.util.stream.Collectors;
import com.neuronrobotics.bowlerstudio.vitamins.Vitamins;
import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Cube;
CSG generate(){
	String type= "joystickCovers"
	if(args==null)
		args=["nintendoSwitch"]
	// The variable that stores the current size of this vitamin
	StringParameter size = new StringParameter(	type+" Default",args.get(0),Vitamins.listVitaminSizes(type))
	HashMap<String,Object> measurments = Vitamins.getConfiguration( type,size.getStrValue())

	def heightValue = measurments.height
	def innerRadiusValue = measurments.innerRadius
	def massCentroidXValue = measurments.massCentroidX
	def massCentroidYValue = measurments.massCentroidY
	def massCentroidZValue = measurments.massCentroidZ
	def massKgValue = measurments.massKg
	def outerRadiusValue = measurments.outerRadius
	def priceValue = measurments.price
	def sourceValue = measurments.source
	for(String key:measurments.keySet().stream().sorted().collect(Collectors.toList())){
		println "joystickCovers value "+key+" "+measurments.get(key);
}
	// Stub of a CAD object
	CSG part = new Cube().toCSG()
	return part
		.setParameter(size)
		.setRegenerate({generate()})
}
return generate() 