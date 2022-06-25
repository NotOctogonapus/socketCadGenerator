import eu.mihosoft.vrl.v3d.parametrics.*;
import java.util.stream.Collectors;
import com.neuronrobotics.bowlerstudio.vitamins.Vitamins;
import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Cube;
CSG generate(){
	String type= "socket"
	if(args==null)
		args=["6mm"]
	// The variable that stores the current size of this vitamin
	StringParameter size = new StringParameter(	type+" Default",args.get(0),Vitamins.listVitaminSizes(type))
	HashMap<String,Object> measurments = Vitamins.getConfiguration( type,size.getStrValue())

	def lowerDiameterMMValue = measurments.lowerDiameterMM
	def massCentroidXValue = measurments.massCentroidX
	def massCentroidYValue = measurments.massCentroidY
	def massCentroidZValue = measurments.massCentroidZ
	def massKgValue = measurments.massKg
	def priceValue = measurments.price
	def sourceValue = measurments.source
	def upperDiameterMMValue = measurments.upperDiameterMM
	def lowerHeightMMValue = measurments.lowerHeightMM
	def upperHeightMMValue = measurments.upperHeightMM
	for(String key:measurments.keySet().stream().sorted().collect(Collectors.toList())){
		println "socket value "+key+" "+measurments.get(key);
}
	// Stub of a CAD object
	def lowerRadiusMM = lowerDiameterMMValue/2
	def upperRadiusMM = upperDiameterMMValue/2
	def cylinderRes = 16
	CSG lowerCylinder = new Cylinder(lowerRadiusMM, lowerRadiusMM, lowerHeightMMValue, cylinderRes).toCSG()
	CSG upperCylinder = new Cylinder(upperRadiusMM, upperRadiusMM, upperHeightMMValue, cylinderRes).toCSG()
	// put the upper cylinder on top of the lower one
	upperCylinder = upperCylinder.movez(lowerCylinder.getMaxZ() - upperCylinder.getMinZ())
	def part = upperCylinder.union(lowerCylinder)
	return part
		.setParameter(size)
		.setRegenerate({generate()})
}
return generate() 