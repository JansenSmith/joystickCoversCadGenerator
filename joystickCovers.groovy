import eu.mihosoft.vrl.v3d.parametrics.*;
import java.util.stream.Collectors;
import com.neuronrobotics.bowlerstudio.vitamins.Vitamins;
import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Cube;
import eu.mihosoft.vrl.v3d.Cylinder
import eu.mihosoft.vrl.v3d.Sphere


CSG generate(){
	String type= "joystickCovers"
	if(args==null)
		args=["nintendoSwitch"]
	// The variable that stores the current size of this vitamin
	StringParameter size = new StringParameter(	type+" Default",args.get(0),Vitamins.listVitaminSizes(type))
	HashMap<String,Object> measurments = Vitamins.getConfiguration( type,size.getStrValue())

	def ballRadiusValue = measurments.ballRadius
	def heightValue = measurments.height
	def innerRadiusValue = measurments.innerRadius
	def massCentroidXValue = measurments.massCentroidX
	def massCentroidYValue = measurments.massCentroidY
	def massCentroidZValue = measurments.massCentroidZ
	def massKgValue = measurments.massKg
	def materialThicknessValue = measurments.materialThickness
	def outerRadiusValue = measurments.outerRadius
	def priceValue = measurments.price
	def sourceValue = measurments.source
	def ballRadiusParam = new LengthParameter("Foot Curvature",ballRadiusValue,[10000,2 * Math.PI * innerRadiusValue])
	def ballRadiusMM = ballRadiusParam.getMM()
	for(String key:measurments.keySet().stream().sorted().collect(Collectors.toList())){
		println "joystickCovers value "+key+" "+measurments.get(key);
}
	// Stub of a CAD object
	CSG part = footBallSection(heightValue, innerRadiusValue, materialThicknessValue, ballRadiusMM)
	return part
		.setParameter(size)
		.setParameter(ballRadiusParam)
		.setRegenerate({generate()})
}
return generate() 

CSG footBallSection(Double heightValue, Double innerRadiusValue, Double materialThicknessValue, Double ballRadiusMM) {
	def arclen=innerRadiusValue
	def capThickness=heightValue/3
	def materialThickness = materialThicknessValue
	def ballRadius = ballRadiusMM
	def radius = ballRadiusMM-(capThickness/2.0)
	def neckRad = 6
	def neckThicknes =3.5
	def theta = (arclen*360)/(2.0*3.14159*radius)
	def internalAngle = (90-(theta/2))
	def d = Math.sin(Math.toRadians(internalAngle))*radius

	//println d +" "+theta+" cir="+(3.14159*radius)+ " ind angle="+internalAngle

	CSG slicer = new Cylinder(radius*2, neckThicknes).toCSG()
			.difference(new Cylinder(neckRad,neckRad+neckThicknes, neckThicknes,15).toCSG())
			.toZMax()
			.movez(d)
	CSG slicer2 = new Cylinder(radius*2, radius*2).toCSG()
			.movez(d-neckThicknes)

	CSG foot = new Sphere(ballRadiusMM,32, 16).toCSG()
			.difference(slicer2)

	CSG ball  = new Sphere(radius,32, 16).toCSG()
			.difference(slicer)
			//.difference(slicer2)
			.union(foot)
	
	CSG pad = new Sphere(radius+materialThickness,32,16).toCSG()
			.intersect(slicer2)
			.difference(ball)
			.toZMin()
	
	//.union(new Cylinder(radius-2, ballRadius).toCSG().toZMax())

//	CSG ret = ball.union(pad)
}