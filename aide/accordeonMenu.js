				$(document).ready( function () {
				// On cache les sous-menus :
				$(".navigation ul.sous-menu").hide();
				// On modifie l'évènement "click" sur les liens dans les items de liste
				// qui portent la classe "sous-menu" :
				$(".navigation li.open-close > a").click( function () {
					// Si le sous-menu était déjà ouvert, on le referme :
					if ($(this).next("ul.sous-menu:visible").length != 0) {
						$(this).next("ul.sous-menu").slideUp("normal", function () { $(this).parent().removeClass("open") });
					}
					// Si le sous-menu est caché, on ferme les autres et on l'affiche :
					else {
						$(".navigation ul.sous-menu").slideUp("normal", function () { $(this).parent().removeClass("open") });
						$(this).next("ul.sous-menu").slideDown("normal", function () { $(this).parent().addClass("open") });
					}
					// On empêche le navigateur de suivre le lien :
					return false;
				});
			} ) ;