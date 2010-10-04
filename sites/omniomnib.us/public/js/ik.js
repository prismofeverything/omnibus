var ik = function() {
    var maxlevel = 20;
    var zoom = 500;
    var move = [400, 200];
    var colorFactor = 127;

    var above = function(level) {
        return (maxlevel - level) > 0 ? (maxlevel - level) : 0;
    };

    var dot = function(a, b) {
        var uu = 0;
        for (var oo = 0; oo < Math.min(a.length, b.length); oo++) {
            uu += a[oo] * b[oo];
        }
        return uu;
    };

    var mul = function(matrix, origin) {
        var ec = [];
        var len = matrix.length / origin.length;
        for (var ii = 0; ii < len; ii++) {
            var base = ii * len;
            var nnn = matrix.slice(base, base + len);
            ec.push(dot(origin, nnn));
        }
        return ec;
    };

    var colorhex = function(color) {
        var hex = '';
        for (var o = 0; o < 3; o++) {
            var trans = Math.min(Math.floor(colorFactor * Math.abs(color[o])), 255).toString(16);
            while (trans.length < 2) {
                trans = '0' + trans;
            }
            hex += trans;
        }
        return hex;
    };

    var transform = function(pos, color, cycle) {
        return {
            pos: pos,
            color: color,
            cycle: cycle
        };
    };

    var randomTransform = function(dim) {
        var trans = [];
        for (var tt = 0; tt < dim; tt++) {
            trans[tt] = (Math.random() * 2) - 1;
        }
        return trans;
    };

    var cyclic = function(pod) {
        if (pod.children.length === 0) {
            var chosen = Math.floor(Math.random() * 3);
            pod.add(chosen);
        }
    };

    var transforms = [];
    for (var ll = 0; ll < 3; ll++) {
        transforms[ll] = transform(randomTransform(4), randomTransform(9), cyclic);
    }

    var shift = function(pos) {
        return [(pos[0] * zoom) + move[0], (pos[1] * zoom) + move[1]];
    };

    var node = function(tag, level, origin, color) {
        return {
            tag: tag,
            level: level,
            pos: mul(transforms[tag].pos, origin),
            color: mul(transforms[tag].color, color),
            children: [],

            add: function(tag) {
                this.children.push(node(tag, this.level + 1, this.pos, this.color));
            },

            draw: function(jji) {
                var shifted = shift(this.pos);

                jji.fillStyle = '#' + colorhex(this.color);
                jji.beginPath();
                jji.arc(shifted[0], shifted[1], 100/(this.level + 1), 0, Math.PI * 2);

                if (above(this.level) > 1) {
                    for (var c = 0; c < this.children.length; c++) {
                        this.children[c].draw(jji);
                    }
                }

                jji.fill();
            },

            cycle: function(cyc) {
                for (var c = 0; c < this.children.length; c++) {
                    var child = this.children[c];
                    child.cycle(transforms[child.tag].cycle);
                }

                cyc(this);
            }
        };
    };

    var cycle = function(jji, roots) {
        for (var r = 0; r < roots.length; r++) {
            var root = roots[r];
            root.draw(jji);
            root.cycle(transforms[root.tag].cycle);
        }
    };

    var ik = function() {
        var mn = document.getElementById('ik');
        mn.width = window.innerWidth;
        mn.height = window.innerHeight;

        var jji = mn.getContext('2d');

        var roots = [];
        for (var oo = 0; oo < 3; oo++) {
            roots[oo] = node(oo, 0, randomTransform(2), randomTransform(3));
        }

        cycle(jji, roots);

        setInterval(function() {cycle(jji, roots);}, 80);
    };

    return {
        ik: ik,
        above: above
    };
}();