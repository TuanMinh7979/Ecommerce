var attributesObject = {};
var filterObject = {};
var isDataLoaded = 0;

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

callAttributeFilterApi(`/admin/category/api/${categoryId}/attribute-filter`);

//clear

$(".rotate").click(function () {
    let setAllCb = $("#setChangeForAllChildCb");
    setAllCb.attr("checked", !setAllCb.attr("checked"));
    $(this).toggleClass("down");
});

// $(".nav-tabs a").on("shown.bs.tab", function (event) {
//     var editMode = $(event.target).text();

// });

$("#addNewAttributeBtn").on("click", function (event) {
    event.preventDefault();
    $("#addModalBtn").click();
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

    $("#exampleModalCenter3").modal("hide");


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


    let checkedCateInView = $(document).find(".selected-viewcateid__cb:checkbox:checked");
    let checkedCateInViewData = "";
    console.log(checkedCateInView)
    if ($(checkedCateInView) != undefined && $(checkedCateInView).length != 0) {

        $(checkedCateInView).each(function () {
            console.log($(this).val());
            checkedCateInViewData += $(this).val();
            checkedCateInViewData += " ";
        })

        $(this).append(`<input type="hidden" name="childrenIdsInView" />`);
    }

    let allHiddenInp = $(this).find('input[type="hidden"]');
    $(allHiddenInp).each(function () {
        if ($(this).attr('name') == "atbs") {
            $(this).val(atbString);
        } else if ($(this).attr('name') == "filter") {
            $(this).val(filterString);
        } else {
            $(this).val(checkedCateInViewData);
        }

    })


    return true;
});


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
            "There are no any attribute!" +
            '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
            '           <span aria-hidden="true">&times;</span>' +
            "        </button>" +
            "</div>"
        $("#attribute-table").before(rs);
    }


}

//COMMON METHOD
function callAttributeFilterApi(url) {
    $.ajax({
        type: "get",
        url: url,
        success: function (data) {
            if (data !== null && data !== undefined) {

                isDataLoaded = 1;
                attributesObject = JSON.parse(data.atbs);
                filterObject = JSON.parse(data.filter);
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

function getAndRenderCheckedFilterOpt(atbClientNames) {
    $.ajax({
        type: "get",
        url: `/admin/category/api/${categoryId}/rootfilter`,
        contentType: "application/json",

        success: function (data) {
            let rootFilter = JSON.parse(data);
            let rs = "";

            atbClientNames.map(function (pubKey) {
                rs += "<div class='filters-item__div'>";
                rs += `<label>${pubKey}</label>`;
                rs += `<div id="${pubKey}OptWrapper" class="filters-item-checkboxes-wrapper">`;

                if (rootFilter[pubKey] !== undefined) {
                    //case filter = 1  but filter column do not have key pubKey
                    let OptKV = rootFilter[pubKey];
                    for (let optK in OptKV) {
                        if (filterObject[pubKey] != undefined && filterObject[pubKey].hasOwnProperty(optK)) {
                            rs += `<input id=${optK} class="ml-3" type='checkbox' checked val="${OptKV[optK]}"/>  <span>${OptKV[optK]}</span>`;
                        } else {
                            rs += `<input id=${optK} class="ml-3" type='checkbox' val="${OptKV[optK]}" /> <span>${OptKV[optK]}</span>`;
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


$("#miniSettingMenuCategory").on("click", function (event) {
    event.preventDefault();

    console.log($(document).find("#loader"));

    $.ajax({
        type: "get",
        url: `/admin/category/api/${categoryId}/menu-category`,
        contentType: "application/json",

        success: function (data) {

            let curIds = data.childrenIdsInView != null ? data.childrenIdsInView : "";
            let avaIds = data.availableChilds
            let rs = "";
            rs += "<ul class='menu-category'>"

            if (curIds != "") {

                avaIds.map(function (avaIdi) {
                    if (curIds.includes(String(avaIdi.id))) {
                        rs += `<li><span>${avaIdi.name}</span><span>${avaIdi.numOfDirectProduct}</span><input value=${avaIdi.id} class="selected-viewcateid__cb" type="checkbox" checked/></li>`
                    } else {
                        rs += `<li><span>${avaIdi.name}</span> <span>${avaIdi.numOfDirectProduct}</span><input value=${avaIdi.id} class="selected-viewcateid__cb" type="checkbox" /></li>`
                    }
                })
            } else {
                avaIds.map(function (avaIdi) {
                    rs += `<li><label>${avaIdi.name}</label> <input value=${avaIdi.id} class="selected-viewcateid__cb" type="checkbox" ></li>`
                })
            }


            rs += "</ul>"


            $("#miniSettingMenuCategoryModal .modal-body").html(rs);

            $('#miniSettingMenuCategoryModal').modal('show');
        },
        error: function () {
            Swal.fire({
                icon: "error",
                title: "Can not load Menu category",
                // text: 'Something went wrong!',
            });
        },


    })
})

$("#miniSettingMenuCategoryModalSaveBtn").on("click", function () {

    $("#miniSettingMenuCategoryModal").modal("hide");
})
