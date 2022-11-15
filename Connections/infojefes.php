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
	  $colname_RstJefes = $_SESSION['MM_Username'];
	}
	mysqli_select_db($cnx,$database_cnx);
	$query_RstJefes = "SELECT * FROM datosjefes WHERE cedulajefe = '$colname_RstJefes'";
	$RstJefes = mysqli_query($cnx,$query_RstJefes) or die(mysql_error());
	$row_RstJefes = mysqli_fetch_assoc($RstJefes);
	$totalRows_RstJefes = mysqli_num_rows($RstJefes);
?>