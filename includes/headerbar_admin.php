<?php session_start(); ?>
<div class="headerbar">
    <!-- LOGO -->
    <div class="headerbar-left">
        <a href="index.html" class="logo"><img alt="logo" src="assets/images/logo.png" /> <span><?php echo 'PANEL'; ?></span></a>
    </div>
    <nav class="navbar-custom">
        
        <ul class="list-inline float-right mb-0">
            <li class="list-inline-item dropdown notif">
                <a class="nav-link dropdown-toggle arrow-none" data-toggle="dropdown" href="#" role="button" aria-haspopup="false" aria-expanded="false">
                    <i class="fa fa-fw fa-question-circle"></i>
                </a>
                <div class="dropdown-menu dropdown-menu-right dropdown-arrow dropdown-arrow-success dropdown-lg">
                    <!-- item-->
                    <div class="dropdown-item noti-title">
                        <h5><small>TIPO DE USUARIO: <?php echo $_SESSION['MM_TipoUser']; ?></small></h5>
                    </div>
                    <!-- All-->
                    <a title="Clcik to visit Pike Admin Website" target="_blank" href="http://mariomendieta.blogspot.com.co/" class="dropdown-item notify-item notify-all">
                        <i class="fa fa-user-circle-o"></i> <h5><small>Nombre: <?php echo $_SESSION['MM_Nombre']; ?></small></h5>
                    </a>
                </div>
            </li>
            </li> 
            <li class="list-inline-item dropdown notif">
                <a class="nav-link dropdown-toggle nav-user" data-toggle="dropdown" href="#" role="button" aria-haspopup="false" aria-expanded="false">
                    <img src="fotosusuarios/<?php echo $row_RstUsuarios['Foto']; ?>" alt="Profile image" class="avatar-rounded">
                </a>
                <div class="dropdown-menu dropdown-menu-right profile-dropdown">
                    <!-- item-->
                    <!-- <div class="dropdown-item noti-title">
                        <h5 class="text-overflow"><small>Bienvenido : <?php echo $_SESSION['MM_Nombre']; ?> </small> </h5>
                    </div> -->
                    <!-- item-->
                    <!-- <a href="perfilusuario_edit.php" class="dropdown-item notify-item">
                        <i class="fa fa-user"></i> <span>Pelfil</span>
                    </a>
                    <a href="cambiarimagenusuario.php" class="dropdown-item notify-item">
                        <i class="fa fa-file-photo-o"></i> <span>Foto</span>
                    </a>
                    <a href="cambiarpass_admin.php" class="dropdown-item notify-item">
                        <i class="fa fa-gears"></i> <span>Contraseña</span>
                    </a>
                    <a href="cambiaruser_admin.php" class="dropdown-item notify-item">
                        <i class="fa fa-user"></i> <span>Usuario</span>
                    </a> -->
                    <!-- item-->
                    <a href="logout.php" class="dropdown-item notify-item">
                        <i class="fa fa-power-off"></i> <span>Salir</span>
                    </a>
                </div>
            </li>
        </ul>
        <ul class="list-inline menu-left mb-0">
            <li class="float-left">
                <button class="button-menu-mobile open-left">
                    <i class="fa fa-fw fa-bars"></i>
                </button>
            </li>                        
        </ul>
    </nav>
</div>
