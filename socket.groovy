import eu.mihosoft.vrl.v3d.parametrics.*;
import java.util.stream.Collectors;
import com.neuronrobotics.bowlerstudio.vitamins.Vitamins;
import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Cube;

// real dimensions
// 
// imperial
// lower diameter 19mm for all
// lower height 12.35mm; upper height 11mm;
// 1/4" 10.5mm upper diameter
// 9/32" 11.75
// 5/16" 12.5
// 3/8" 14.5
// 7/16" 17
// 
// lower height 9.5mm; upper height 13mm;
// 1/2" 20
// 9/16" 21
// 5/8" 23.5
// 11/16" 24
// 3/4" 25.75
// 
// metric
// lower diameter 19mm for all
// lower height 12.5mm; upper height 10.5mm;
// 6mm 10.5 upper diameter
// 7mm 11.5
// 8mm 12.75
// 9mm 14
// 10mm 15
// 11mm 17
// 12mm 18
// 
// lower height 9.5mm; upper height 13mm;
// 13mm 20
// 14mm 21.5
// 15mm 22
// 16mm 23.5
// 17mm 24
// 18mm 25
// 19mm 26
// 
// 
// spline
// lower diameter 19.5mm for all
// lower height 24mm; upper height 8.5mm;
// 10mm 15 upper diameter
// 11mm 17
// 12mm 18
// 13mm 19.5
// 
// 14mm 23 upper diameter; lower height 13.25mm; upper height 18.3mm;
// 15mm 23.5 lower height 13.25mm; upper height 19.5mm;
// 16mm 23.5 lower height 13mm; upper height 19.75mm;
// 17mm 4.75 lower height 12.5mm; upper height 20.5mm;
// 18mm 24.75 lower height 13mm; upper height 20.5mm;
// 19mm 27.75 lower height 13mm; upper height 22.75mm;

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
