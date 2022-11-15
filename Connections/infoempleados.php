<?php
	//initialize the session
	if (!isset($_SESSION)) 
	{
	session_start();
	$username=$_SESSION['MM_Username'];
	if ($_SESSION["autentificado"] != "SI") 
	{ 
	//si no está logueado lo envío a la página de autentificación 
	header("Location: index.php"); 
	}
	$frase=$_SESSION['frase'];
	//echo $frase;
	}
?>
<?php
	if (isset($_SESSION['MM_Username'])) 
	{
	  $colname_RstEmpleados = $_SESSION['MM_Username'];
	}
	mysqli_select_db($cnx,$database_cnx);
	$query_RstEmpleados = "SELECT * FROM empleados WHERE cedulaempleado = '$colname_RstEmpleados'";
	$RstEmpleados = mysqli_query($cnx,$query_RstEmpleados) or die(mysql_error());
	$row_RstEmpleados = mysqli_fetch_assoc($RstEmpleados);
	$totalRows_RstEmpleados = mysqli_num_rows($RstEmpleados);
?>