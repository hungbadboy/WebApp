function add_placeholder (id, placeholder)
{
  var el = document.getElementById(id);
  el.placeholder = placeholder;
 
    el.onfocus = function ()
    {
    if(this.value == this.placeholder)
    {
      this.value = '';
      el.style.cssText  = '';
    }
    };
 
    el.onblur = function ()
    {
    if(this.value.length === 0)
    {
      this.value = this.placeholder;
      el.style.cssText = 'color:#A9A9A9;';
    }
    };
 
  el.onblur();
}
// Add right before </body> or inside a DOMReady wrapper