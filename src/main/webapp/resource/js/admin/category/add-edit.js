var attributesObject = {};
var filterObject = {};

$(function () {
    $.ajax({
        type: "get",
        url: "/ajax/hierarchical-category",
        success: function (data) {
            renderCatHierarchical(data);
        },
        error: function () {
            Swal.fire({
                icon: "error",
                title: "Can not load this categories in hierarchical",
                // text: 'Something went wrong!',
            });
        },
    });
});

//clear

$(".rotate").click(function () {
    let setAllCb = $("#setChangeForAllChildCb");
    setAllCb.attr("checked", !setAllCb.attr("checked"));
    $(this).toggleClass("down");
});

$(".nav-tabs a").on("shown.bs.tab", function (event) {
    var editMode = $(event.target).text();

    if (editMode == "Detail") {
        callAttributeApi(`/admin/category/api/${categoryId}/attributes`);

    }
});

$("#addNewAttributeBtn").on("click", function (event) {
    event.preventDefault();
    $("#addModalBtn").click();
});

$("#resetOriAttributeBtn").on("click", function (event) {
    event.preventDefault();
    resetOriAtb();

});


$("#tag_delete_many").on("click", function (event) {
    event.preventDefault();
    let checkboxes = $('tr input[type="checkbox"]:checked');
    $(checkboxes).each(function () {
        let keyToDel = $(this)
            .parent()
            .parent()
            .find(".atb-iddel-checkbox")
            .val();
        delete attributesObject[keyToDel];
        $(this).closest("tr").remove();
    })
});


$(document).on("click", ".tag_delete_one", function (event) {
    event.preventDefault();
    let keyToDel = $(this)
        .parent()
        .parent()
        .find(".atb-iddel-checkbox")
        .val();
    // removeAttributeByName(nameToDel, attributesObject);
    delete attributesObject[keyToDel];

    $(this).closest("tr").remove();
});

$("#saveCurFiltersBtn").on("click", function () {
    $(".filters-item-checkboxes-wrapper").each(function () {
        let curFilterValue = {};

        let allCbox = $(this).find("input[type='checkbox']:checked");

        let atbiName = $(this).attr("id");
        atbiName = atbiName.slice(0, -10);

        if (allCbox.length != 0) {
            $(allCbox).each(function () {
                let optionKey = $(this).attr("id");

                let optionClientVal = $(this).attr("val");

                if (optionClientVal != undefined) {
                    curFilterValue[optionKey] = optionClientVal;
                }


            });

            if (curFilterValue !== undefined && Object.keys(curFilterValue).length !== 0) {
                filterObject[atbiName] = curFilterValue;
            }
        }
        //   attributesObject[atbiName].filterValue = curFilterValue;

    });
    if ($("#setChangeForAllChildCb").is(":checked") == false) {
        $("#exampleModalCenter3").modal("hide");
    } else {
        let url = `/admin/category/api/${categoryId}/filterset-forallchild`;
        $("#exampleModalCenter3").modal("hide");
        document.getElementById("loader").style.display = "block";
        $.ajax({
            type: "post",
            url: url,
            contentType: "application/json",
            data: JSON.stringify(filterObject),

            success: function (res) {
                document.getElementById("loader").style.display = "none";

            },
            error: function () {
                Swal.fire({
                    icon: "error",
                    title: "Can not call this Api",
                    // text: 'Something went wrong!',
                });
            },
        });
    }


});
// });
//end here

//
$("#addAtbConfirmBtn").on("click", function (event) {
    event.preventDefault();
    saveCurrentFormAttribute(this);
});

$("#updateAtbConfirmBtn").on("click", function (event) {
    event.preventDefault();
    saveCurrentFormAttribute(this);
});

//


$('#mainForm').submit(function () {
    let atbString = JSON.stringify(attributesObject);
    let filterString = JSON.stringify(filterObject);
    // console.log(atbString);

    $(this).append(`<input type="hidden" name="atbs" />`);
    $(this).append(`<input type="hidden" name="filter" />`);

    let allHiddenInp = $(this).find('input[type="hidden"]');
    $(allHiddenInp).each(function () {
        if ($(this).attr('name') == "atbs") {
            $(this).val(atbString);
        } else {
            $(this).val(filterString);
        }

    })
    return true;
});


function resetOriAtb() {
    // `/admin/category/api/${categoryId}/attributes`
    document.getElementById("loader").style.display = "block";
    $.ajax({
        type: "post",
        url: `/admin/category/api/${categoryId}/attributes/update/reset-ori-atb`,
        contentType: "application/json",

        success: function (newAtb) {
            attributesObject = newAtb;
            document.getElementById("loader").style.display = "none";

            location.reload();

        },
        error: function () {
            Swal.fire({
                icon: "error",
                title: "Can not call this Api",
                // text: 'Something went wrong!',
            });
        },
    });
}

function renderCatHierarchical(data) {
    let opts = "";
    data.map(function (cat) {
        if (typeof categoryParentId !== "undefined") {
            if (cat.id != categoryParentId) {
                opts += `<option value="${cat.id}">${cat.name}</option> `;
            } else {
                opts += `<option value="${cat.id}" selected>${cat.name}</option> `;
            }
        } else {
            opts += `<option value="${cat.id}">${cat.name}</option> `;
        }
    });
    $(".parent-sel").append(opts);
}

$(document).on("click", ".push", function (event) {
    event.preventDefault();
    pushNewRecord(this);
});
$(document).on("click", ".delete", function (event) {
    event.preventDefault();
    $(this).parent().remove();
});

$(document).on("click", ".editAttributeBtn", function (event) {
    event.preventDefault();
    //load data first
    loadAttributeToForm(this);
    //after that click modal btn
    $("#editModalBtn").click();
});

function renderDataForAttributeTable(atbs) {
    let rs = "";

    // alert(Object.keys(attributesObject).length)
    if (Object.keys(atbs).length != 0) {
        for (let atbi in atbs) {
            let curAtr = atbs[atbi];

            rs += `       <tr class="col-12">
                <td class="col-1"><input type="checkbox" class="atb-iddel-checkbox" value="${atbi}"></td>
                <td class="col-5" class="atb-name-inp" ">${atbi}</td>
                <td class="col-3"><i class="atb-active-checkbox fas fa-circle" isactive="${curAtr.active}"></i>  </td>
                  <td class="col-3">
                    <button class="editAttributeBtn btn btn-default" >Edit</button>
                    <a class="btn btn-danger tag_delete_one"  >Delete</a>

                </td>

            </tr>`;
        }
        $("#tabledata").html(rs);
        setActiveCheckbox();
    } else {
        rs += "<div class=\"alert alert-warning\" role=\"alert\">\n" +
            "There are not any attribute!" +
            '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
            '           <span aria-hidden="true">&times;</span>' +
            "        </button>" +
            "</div>"
        $("#attribute-table").before(rs);
    }


}

//COMMON METHOD
function callAttributeApi(url) {
    $.ajax({
        type: "get",
        url: url,
        success: function (data) {

            if (data !== null && data !== undefined) {
                filterObject = JSON.parse(data.filter);
                attributesObject = JSON.parse(data.atbs);
                // if (Object.keys(attributesObject).length !== 0) {
                renderDataForAttributeTable(attributesObject);
            }
            // } else {
            //     alert("This category do not have any attribute");
            // }
        },
        error: function () {
            Swal.fire({
                icon: "error",
                title: "Can not call this Api",
                // text: 'Something went wrong!',
            });
        },
    });
}

function loadAttributeToForm(editAttributeBtn) {
    let tri = $(editAttributeBtn).parent().parent();
    let triKeyName = tri.find(".atb-iddel-checkbox").val();

    //
    let isfilter = attributesObject[`${triKeyName}`].filter;
    let isactive = tri.find(".atb-active-checkbox").attr("isactive");

    let editModal = $("#exampleModalCenter2");
    editModal.find(".atrNameInp").val(triKeyName);
    editModal
        .find(".atrActiveChbx")
        .prop("checked", isactive == 1 ? true : false);
    editModal
        .find(".atrFilterChbx")
        .prop("checked", isfilter == 1 ? true : false);

    let valueList = editModal.find(".tasks");

    let currentAttribute = attributesObject[triKeyName];

    if (currentAttribute["value"] != undefined) {
        let rs = "";
        currentAttribute["value"].map(function (valuei) {
            rs += `<div class="task">
                <span class="atrValueSpan" class="taskname">
                    ${valuei}
                </span>
                        <button class="delete">
                            <i class="far fa-trash-alt"></i>
                        </button>
                    </div>`;
        });
        valueList.html(rs);
    }
}

function saveCurrentFormAttribute(saveBtn) {
    let data = {};
    let modalContent = $(saveBtn).parent().parent();
    let attributeWrapper = modalContent.find(".attributeWrapper");

    let keyname = attributeWrapper.find(".atrNameInp").val();
    data["active"] = attributeWrapper.find(".atrActiveChbx").is(":checked")
        ? 1
        : 0;
    data["filter"] = attributeWrapper.find(".atrFilterChbx").is(":checked")
        ? 1
        : 0;
    let valueArr = [];
    attributeWrapper.find(".atrValueSpan").each(function () {
        valueArr.push(
            $(this)
                .text()
                .replace(/\r?\n|\r/g, " ")
                .trim()
        );
    });
    data["value"] = valueArr;
    let curModal = saveBtn.closest(".modal");
    if ($("#exampleModalCenter1").hasClass("show") && existByName(keyname, attributesObject)) {
        alert(keyname + " is exist!");
        $(curModal).modal("hide");
        return;
    } else {
        //add new
        attributesObject[keyname] = data;
    }

    $(curModal).modal("hide");
    renderDataForAttributeTable(attributesObject);
}

//FOR CUSTOM ATTRIBUTE FILTER
$("#editFilterBtn").on("click", function (event) {
    event.preventDefault();
    loadFilterData();
});

function getAndRenderCheckedFilterOpt(atbKeyNames) {
    let requestData = {};
    requestData["categoryId"] = categoryId;
    requestData["atbNames"] = atbKeyNames;
    $.ajax({
        type: "post",
        url: "/admin/categoryfilter/list-map",
        data: JSON.stringify(requestData),
        contentType: "application/json",

        success: function (data) {
            if (data === null || data === undefined || data == "") {
                alert("System do not have any filter option for this category");
                return;
            } else {
                let rs = "";
                atbKeyNames.map(function (atbKeyName) {
                    rs += "<div class='filters-item__div'>";
                    rs += `<label>${atbKeyName}</label>`;
                    rs += `<div id="${atbKeyName}OptWrapper" class="filters-item-checkboxes-wrapper">`;

                    if (data[atbKeyName] !== undefined) {
                        //case filter = 1  but filter column do not have key atbKeyName

                        let keyClientVal = filterObject[atbKeyName];
                        for (let optionDataKeyName in data[atbKeyName]) {
                            let dataKeyClientVal = data[atbKeyName];
                            if (keyClientVal != undefined && keyClientVal.hasOwnProperty(optionDataKeyName)) {
                                rs += `<input id=${optionDataKeyName} class="ml-3" type='checkbox' checked val="${dataKeyClientVal[optionDataKeyName]}"/>  <span>${dataKeyClientVal[optionDataKeyName]}</span>`;
                            } else {
                                rs += `<input id=${optionDataKeyName} class="ml-3" type='checkbox' val="${dataKeyClientVal[optionDataKeyName]}" /> <span>${dataKeyClientVal[optionDataKeyName]}</span>`;
                            }
                        }
                    } else {
                        rs += "<p>System do not have any option for this attribute</p>";
                    }

                    rs += "</div>";
                    rs += "</div>";
                });
                // console.log(attributesObject);

                $("#filterWrapper").html(rs);
                $("#editFilterModalBtn").click();
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

function loadFilterData() {
    let atbKeyNames = [];
    for (let atbKeyName in attributesObject) {
        let atbKeyObj = attributesObject[atbKeyName];
        if (atbKeyObj.hasOwnProperty("filter") && atbKeyObj.filter == 1) {
            atbKeyNames.push(atbKeyName);
        }
    }
    if (atbKeyNames.length == 0) {
        alert(
            "There are no attributes that is ticked in it's filter field, please tick one before!"
        );
        return
    } else {
        getAndRenderCheckedFilterOpt(atbKeyNames);
    }
}
