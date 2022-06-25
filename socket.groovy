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
	def cylinderRes = new LengthParameter("Cylinder Resolution", 16, [128.0, 3.0])

	def lowerRadiusMM = lowerDiameterMMValue/2
	def upperRadiusMM = upperDiameterMMValue/2
	CSG lowerCylinder = new Cylinder(lowerRadiusMM, lowerRadiusMM, lowerHeightMMValue, (int)cylinderRes.getMM()).toCSG()
	CSG upperCylinder = new Cylinder(upperRadiusMM, upperRadiusMM, upperHeightMMValue, (int)cylinderRes.getMM()).toCSG()
	// put the upper cylinder on top of the lower one
	upperCylinder = upperCylinder.movez(lowerCylinder.getMaxZ() - upperCylinder.getMinZ())
	def part = upperCylinder.union(lowerCylinder)
	return part
		.setParameter(size)
		.setParameter(cylinderRes)
		.setRegenerate({generate()})
}
return generate() 