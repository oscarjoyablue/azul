<?php include('includes/contadores.php'); ?> 
<?php
    //initialize the session
    // if (!isset($_SESSION)) 
    // {
    //session_start();
    $username=$_SESSION['MM_Username'];
    $TipoUsuario=$_SESSION['MM_TipoUser'];
    // }
?>
<div class="left main-sidebar">
    <div class="sidebar-inner leftscroll">
        <div id="sidebar-menu">
            <ul>
                <li class="submenu">
                    <a href="controlpanel_admin.php"><i class="fa fa-home"></i><span> Inicio </span> </a>
                </li>
                <li class="submenu">
                    <a href="candidatos_control.php"><i class="fa fa-user-circle"></i><span> Panel Candidatos </span> <span class="label radius-circle bg-primary float-right"><?php echo 6; ?></span></a>
                </li>
                <li class="submenu">
                    <a href="categorias_control.php"><i class="fa fa-user-circle"></i><span> Panel Categorías </span> <span class="label radius-circle bg-primary float-right"><?php echo 6; ?></span></a>
                </li>
                <li class="submenu">
                    <a href="#"><i class="fa fa-fw fa-area-chart"></i> <span> Graficas </span> <span class="menu-arrow"></span></a>
                    <ul class="list-unstyled">
                        <li><a href="#">Grafica Columnas</a></li>
                        <li><a href="#">Grafica de Torta</a></li>
                    </ul>
                </li>
                <li class="submenu">
                    <a href="#"><i class="fa fa-trash"></i><span> Eliminar Votos </span> </a>
                </li>
                <li class="submenu">
                    <a href="logout.php"><i class="fa fa-sign-out"></i><span> Salir </a>
                </li>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="clearfix"></div>
    </div>
</div>