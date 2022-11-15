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
	  $colname_RstUsuarios = $_SESSION['MM_Username'];
	}
	mysqli_select_db($cnx,$database_cnx);
	$query_RstUsuarios = "SELECT * FROM usuarios WHERE UserName = '$colname_RstUsuarios'";
	$RstUsuarios = mysqli_query($cnx,$query_RstUsuarios) or die(mysql_error());
	$row_RstUsuarios = mysqli_fetch_assoc($RstUsuarios);
	$totalRows_RstUsuarios = mysqli_num_rows($RstUsuarios);
?>