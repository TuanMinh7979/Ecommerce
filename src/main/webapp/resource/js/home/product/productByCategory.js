$(function () {

    loadFilterUI();
    $('#select').on('change', function () {
        if ($(this).val() === 'important') {
            $(this).addClass('red')
        }
    })
    $(".list-sort button").on("click", function (event) {
        event.preventDefault();
        $(".list-sort button").removeClass("active");
        $(this).toggleClass("active");
    })

})


//
function loadFilterUI() {
    $.ajax({
        type: "get",
        url: `/admin/category/api/${categoryId}/attributes`,
        contentType: "application/json",
        success: function (atbs) {
            console.log(atbs);

            renderFilterUI(atbs)
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Can not load content',
                // text: 'Something went wrong!',

            })
        }


    });

}

function renderFilterUI(atbs) {
    let atbObj = JSON.parse(atbs);
    let filterAtbs = {};
    for (let atbKeyName in atbObj) {
        if (atbObj[atbKeyName].hasOwnProperty("filter") && atbObj[atbKeyName].filter == 1 && Object.keys(atbObj[atbKeyName].filterValue).length !== 0) {
            filterAtbs[`${atbKeyName}`] = atbObj[atbKeyName].filterValue;
        }
    }
    $.ajax({
        type: "post",
        url: `/filter/ui-opt-name`,
        data: JSON.stringify(Object.keys(filterAtbs)),
        contentType: "application/json",
        success: function (data) {
            let kvAtbUiName = data;
            let rs = "";
            for (let filterAtbI in filterAtbs) {
                rs += `<div class="col-2">
                <div class="dropdown">
                <button style="border :1px solid #D3D3D3"
                        class="btn dropdown-toggle"
                        type="button"
                        id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                
                ${kvAtbUiName[filterAtbI]}
                <!--ch-->
                </button>
                <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">`;
                let optionKVs = filterAtbs[filterAtbI];

                for (let optionKi in optionKVs) {
                    rs += `<a id="${optionKi}" class="dropdown-item"  href="#">${optionKVs[optionKi]}</a>`
                }

                rs += `</div>
            </div>

            </div>`;

            }


            $("#listFilter").html(rs);


        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Can not load content',
                // text: 'Something went wrong!',

            })
        }


    });


}