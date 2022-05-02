$(function () {
    loadFilter();
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
function loadFilter() {

    ajaxGet(`/ajax/category/${categoryId}/attributes`, renderFilter);
}

function renderFilter(data) {
    let oriObject = JSON.parse(data);
    let filterData = {};
    for (let atbKeyName in oriObject) {
        for (let valOfAtbKeyName in oriObject[atbKeyName]) {
            if (valOfAtbKeyName == "filter" && oriObject[atbKeyName].filter == 1) {
                filterData[`${atbKeyName}`] = oriObject[atbKeyName].filterValue;
            }
        }

    }
    let rs = "";
    for (let atbOptions in filterData) {
        rs += `<div class="col-2">
        <div class="dropdown">
        <button style="border :1px solid #D3D3D3"
                class="btn dropdown-toggle"
                type="button"
                id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        ${atbOptions}
        </button>
        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">`;
        let optionKVObj = filterData[atbOptions];

        for (let optioniKey in optionKVObj) {
            rs += `<a id="${optioniKey}" class="dropdown-item"  href="#">${optionKVObj[optioniKey]}</a>`
        }

        rs += `</div>
    </div>

    </div>`;

    }


    $("#listFilter").html(rs);


}