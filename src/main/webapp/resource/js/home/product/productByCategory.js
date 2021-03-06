$(function () {


    $("#select").on("change", function () {
        if ($(this).val() === "important") {
            $(this).addClass("red");
        }
    });
    $(".list-sort button").on("click", function (event) {
        event.preventDefault();
        $(".list-sort button").removeClass("active");
        $(this).toggleClass("active");
    });
});
loadFilterUI();


//
function loadFilterUI() {
    let curUrl = window.location.href;
    let categoryId = "";
    if (curUrl.indexOf("?") == -1) {
        categoryId = curUrl.substring(curUrl.lastIndexOf(".") + 1);
    } else {
        categoryId = curUrl.substring(curUrl.lastIndexOf(".") + 1, curUrl.indexOf("?"));
    }

    let newurl = '/ajax/category/' + `${categoryId}` + '/filter';
    // console.log(newurl);
    $.ajax({
        type: "get",
        url: newurl,
        contentType: "application/json",
        success: function (filterSet) {

            renderFilterUI(filterSet, categoryId);
        },
        error: function () {
            Swal.fire({
                icon: "error",
                title: "Can not load Filter UI",
                // text: 'Something went wrong!',
            });
        },
    });
}

function renderFilterUI(filterSet, categoryId) {
    let filterSetKObjs = JSON.parse(filterSet);

    $.ajax({
        type: "post",
        url: `/ajax/filter/ui-opt-name`,
        data: JSON.stringify(Object.keys(filterSetKObjs)),
        contentType: "application/json",
        success: function (data) {
            let kvAtbUiName = data;
            let rs = "";
            for (let filterKi in filterSetKObjs) {
                rs += `<div id=${filterKi} class="col-2 filter-option-key">
                <div class="dropdown">
                <button style="border :1px solid #D3D3D3"
                        class="btn dropdown-toggle"
                        type="button"
                        id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                
                ${kvAtbUiName[filterKi]}
                <!--ch-->
                </button>
                <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">`;
                let optionKVs = filterSetKObjs[filterKi];

                for (let optionKi in optionKVs) {
                    rs += `<a id="${optionKi}" onclick ="filterOptionValueClick(this, event)" class="dropdown-item filter-option-value"  href="#">${optionKVs[optionKi]}</a>`;
                }

                rs += `</div>
            </div>

            </div>`;
            }

            $("#listFilter").html(rs);
            if (categoryFilter != undefined) {
                renderSelectedTag(kvAtbUiName);
            }
        },
        error: function () {
            Swal.fire({
                icon: "error",
                title: "Can not load content",
                // text: 'Something went wrong!',
            });
        },
    });
}

function filterOptionValueClick(thisAtag, event) {
    event.preventDefault();

    let namepar = $(thisAtag).closest(".filter-option-key").attr("id");
    let newParam = "&" + namepar + "=" + $(thisAtag).attr("id");

    let oldurl = window.location.href;
    let newurl = "";
    if (oldurl.indexOf(namepar) != -1) {
        let startIdx =
            oldurl.indexOf(namepar) +
            oldurl.substring(oldurl.indexOf(namepar)).indexOf("=");
        let endIdx = oldurl.indexOf(namepar);
        let endIdxFromName = oldurl.substring(endIdx).indexOf("&");
        let caseIsLastParName =
            endIdxFromName == -1
                ? oldurl.length - 1 - oldurl.indexOf(namepar)
                : endIdxFromName;
        endIdx += caseIsLastParName;

        if (endIdxFromName != -1) {
            newurl =
                oldurl.substring(0, startIdx + 1) +
                $(thisAtag).attr("id") +
                oldurl.substring(endIdx);
        } else {
            newurl = oldurl.substring(0, startIdx + 1) + $(thisAtag).attr("id");
        }
    } else {
        newurl = window.location.href + newParam;
    }

    let idxOfQuestionMark = newurl.indexOf("?");
    if (idxOfQuestionMark == -1) {
        let firstIdx = newurl.indexOf("&");
        newurl =
            newurl.substring(0, firstIdx) + "?" + newurl.substring(firstIdx + 1);
    }
    window.location.href = newurl;
}

function renderSelectedTag(kvAtbUiName) {
    let params = new URL(document.location).searchParams;
    let keys = {};


    let listPar = Array.from(params.keys());

    if (listPar.indexOf('sortBy') > -1) {
        listPar.splice(listPar.indexOf('sortBy'), 1);
    }


    if (listPar.indexOf('sortDir') > -1) {
        listPar.splice(listPar.indexOf('sortDir'), 1);
    }
    listPar.map((function (pari, index) {
        keys[pari] = params.get(pari);
    }))


    let filter = JSON.parse(categoryFilter);
    let rs = "";
    for (let parK in keys) {
        rs += `<div class="filter-tag">
        ${kvAtbUiName[parK]} : ${filter[parK][keys[parK]]}
<!--        <button type="button" id ="${keys[parK]}" class="filter-tag-close close" data-dismiss="alert" aria-label="Close">-->
        <button type="button" id ="${parK}" class="filter-tag-close close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
        </div>
        </div>`;
        $("#listFilterBy").html(rs);
    }
}


$(document).on("click", ".filter-tag-close", function (event) {
    event.preventDefault();
    let param = $(this).attr("id");
    let newurl = removeUrlPar(param);

    window.location.href = newurl;
});

function removeUrlPar(param) {
    let oldurl = window.location.href;

    let curSub = oldurl.substring(oldurl.indexOf(param) - 1);
    let isInter = curSub.substring(1).indexOf("&") == -1 ? false : true
    if (isInter) {
        let start = oldurl.indexOf(curSub);
        let end = start + curSub.substring(1).indexOf("&") + 1
        let replacePart = oldurl.substring(start, end);
        oldurl = oldurl.replace(replacePart, "");
    } else {
        oldurl = oldurl.replace(curSub, "");
    }
    if (oldurl.indexOf("?") == -1) {
        oldurl = oldurl.replace("&", "?");
    }
    return oldurl
}


function sortBtn(sortBy, sortDir) {
    let oldurl = window.location.href;
    if (oldurl.indexOf("sortBy") == -1) {
        oldurl += "&sortBy=" + sortBy;
    } else {
        let curSub = oldurl.substring(oldurl.indexOf("sortBy"));
        let oldSortBy = curSub.substring(curSub.indexOf("=") + 1, curSub.indexOf("&") != -1 ? curSub.indexOf("&") : curSub.length);
        if (oldSortBy != sortBy) {
            oldurl = oldurl.replace(oldSortBy, sortBy);
        }
    }

    if (oldurl.indexOf("sortDir") == -1) {
        oldurl += "&sortDir=" + sortDir;
    } else {
        let curSub = oldurl.substring(oldurl.indexOf("sortDir"));
        let oldSortDir = curSub.substring(curSub.indexOf("=") + 1, curSub.indexOf("&") != -1 ? curSub.indexOf("&") : curSub.length);
        if (oldSortDir != sortDir) {
            oldurl = oldurl.replace(oldSortDir, sortDir);
        }
    }

    if (oldurl.indexOf("?") == -1) {
        oldurl = oldurl.replace("&", "?");
    }

    window.location.href = oldurl;
}
