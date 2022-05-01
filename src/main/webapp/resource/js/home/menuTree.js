var builddata=function (data) {
    var source = [];
    var items = [];
    // build hierarchical source.
    for (i = 0; i < data.length; i++) {
        var item = data[i];


        var label = item["name"];
        var parentid = item["parentId"];
        var id = item["id"];
        var slug = item["code"];

        if (items[parentid]) {
            var item = {parentid: parentid, label: label, item: item, slug: slug};
            if (!items[parentid].items) {
                items[parentid].items = [];
            }
            items[parentid].items[items[parentid].items.length] = item;
            items[id] = item;
        } else {
            items[id] = {parentid: parentid, label: label, item: item, slug: slug};
            source[id] = items[id];
        }
    }
    return source;
}

// var source = builddata();

var buildUL = function (parent, items) {
    $.each(items, function () {
        if (this.label) {

            var li = $(`<li onclick="categoryClick(event)" class="menu__ul__li" catlink="/category/${this.slug}">${this.label}</li>`);


            li.appendTo(parent);
            // if there are sub items, call the buildUL function.
            if (this.items && this.items.length > 0) {
                var ul = $("<ul class='menu__ul'></ul>");
                ul.appendTo(li);
                buildUL(ul, this.items);
            }
        }
    });
}
// var ul = $("<ul></ul>");
// ul.appendTo("#jqxMenu");
// buildUL(ul, source);

// $("#jqxMenu").jqxTree({ width: '300', height: '300px'});