document.addEventListener("DOMContentLoaded", function () {

    const camposCpf = document.querySelectorAll(".mascara-cpf");
    const camposTelefone = document.querySelectorAll(".mascara-telefone");

    camposCpf.forEach(function (campo) {

        campo.addEventListener("input", function () {

            let valor = campo.value.replace(/\D/g, "");

            valor = valor.substring(0, 11);

            valor = valor.replace(
                /(\d{3})(\d)/,
                "$1.$2"
            );

            valor = valor.replace(
                /(\d{3})(\d)/,
                "$1.$2"
            );

            valor = valor.replace(
                /(\d{3})(\d{1,2})$/,
                "$1-$2"
            );

            campo.value = valor;
        });

    });


    camposTelefone.forEach(function (campo) {

        campo.addEventListener("input", function () {

            let valor = campo.value.replace(/\D/g, "");

            valor = valor.substring(0, 11);

            if (valor.length <= 10) {

                valor = valor.replace(
                    /^(\d{2})(\d)/,
                    "($1) $2"
                );

                valor = valor.replace(
                    /(\d{4})(\d)/,
                    "$1-$2"
                );

            } else {

                valor = valor.replace(
                    /^(\d{2})(\d)/,
                    "($1) $2"
                );

                valor = valor.replace(
                    /(\d{5})(\d)/,
                    "$1-$2"
                );
            }

            campo.value = valor;
        });

    });

});