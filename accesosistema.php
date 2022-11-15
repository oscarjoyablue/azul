<?php require_once('Connections/cnx.php'); ?>
<?php
// *** Validate request to login to this site.
//if (!isset($_SESSION)) 
//{
session_start();
$_SESSION["autentificado"]= "SI"; 
if (isset($_POST['Usuario'])) 
{
	$LoginUsername=$_POST['Usuario'];
	$Password=$_POST['Password'];
	$MM_redirectLoginSuccessAdmin = "controlpanel_admin.php";
	$MM_redirectLoginFailed = "noexiste.php";
	mysqli_select_db($cnx,$database_cnx);
	$LoginRS__query="SELECT * FROM usuarios 
	WHERE UserName='$LoginUsername' AND Pass='$Password'";
	$LoginRS = mysqli_query($cnx,$LoginRS__query) or die(mysql_error());
	$row_LoginRS = mysqli_fetch_assoc($LoginRS);
	$loginFoundUser = mysqli_num_rows($LoginRS);
	$Nivel=$row_LoginRS['Nivel'];
	$Nombre=$row_LoginRS['Nombre'];
	// echo 'Password: '.$Password.'<br>';
	// echo 'LoginUsername: '.$LoginUsername.'<br>';
	// echo 'TipoUser: '.$TipoUser.'<br>';
	if($loginFoundUser>0)
	{
		$_SESSION['MM_Username'] = $LoginUsername;
		$_SESSION['MM_TipoUser'] = $TipoUser;
		$_SESSION['MM_Nombre'] = $Nombre;
		header("Location: " . $MM_redirectLoginSuccessAdmin);
	}else{
		header("Location: noexiste.php" );	
	} 
  }
?>
