<?php include('includes/contadores.php'); ?> 
<div class="left main-sidebar">
    <div class="sidebar-inner leftscroll">
        <div id="sidebar-menu">
            <ul>
                <li class="submenu">
                    <a href="controlpanel_superadmin.php"><i class="fa fa-fw fa-bars"></i><span> Dashboard </span> </a>
                </li>
                <li class="submenu">
                    <a href="usuarios_control.php"><i class="fa fa-users"></i><span> Panel Usuarios </span> <span class="label radius-circle bg-primary float-right"><?php echo $numero_usuarios; ?></span></a>
                </li>
                <li class="submenu">
                    <a href="perfilusuario_edit.php"><i class="fa fa-vcard-o"></i><span> Cambiar Perfil </span> </a>
                </li>
                <li class="submenu">
                    <a href="cambiarimagenusuario.php"><i class="fa fa-user-circle-o"></i><span> Cambiar Foto </span> </a>
                </li>
                <li class="submenu">
                    <a href="cambiarpass_admin.php"><i class="fa fa-certificate"></i><span> Cambiar Contraseña </span> </a>
                </li>
                <!-- <li class="submenu">
                    <a href="cambiaruser_admin.php"><i class="fa fa-user"></i><span> Cambiar Usuario </span> </a>
                </li> -->
                <li class="submenu">
                    <a href="logout.php"><i class="fa fa-sign-out"></i><span> Salir </span> </a>
                </li>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="clearfix"></div>
    </div>
</div>