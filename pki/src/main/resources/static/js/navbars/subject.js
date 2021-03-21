$(document).ready(function () {
    $('body').prepend($(
        '<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">'
        + '<div class="collapse navbar-collapse" id="navbarNav">'
        + ' <ul class="navbar-nav">'
        + '  <li class="nav-item">'
        + '  <a class="nav-link" href="/html/subject_show_all.html">Show all</a>'
        + '  </li>'
         + '  <li class="nav-item">'
        + '  <a class="nav-link" href="/html/subject_info.html">Info</a>'
        + '  </li>'
        + ' <li class="nav-item">'
        + '  <a href="javascript:logOut();" class="nav-link">Log out</a>'
        + '  </li>'
        + ' </ul>'
        + ' </div>'
        + ' </nav>'
    ));
});