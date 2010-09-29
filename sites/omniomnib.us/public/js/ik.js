var ik = function() {
    var maxlevel = 5;
    var zoom = 500;

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
            var trans = Math.min(Math.floor(255 * color[o]), 255).toString(16);
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

    var transforms = {
        aa: {
            pos: [0.3, 0.8, 0.7, 0.1],
            color: [0.1, 0.3, 0.5, 0.4, 0.3, 0.4, 0.2, 0.2, 0.3],
            cycle: function(pod) {
                if (pod.children.length === 0) {
                    pod.add(pod.tag);
                }
            }
        }
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
                jji.fillStyle = '#' + colorhex(this.color);
                jji.beginPath();
                jji.arc(this.pos[0] * zoom, this.pos[1] * zoom, above(this.level) * 5, 0, Math.PI * 2);

                if (above(this.level) > 1) {
                    for (var c = 0; c < this.children.length; c++) {
                        this.children[c].draw(jji);
                    }
                }

                jji.fill();
            },

            cycle: function(cyc) {
                for (var c = 0; c < this.children; c++) {
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

        var roots = [node('aa', 0, [0.5, 0.5], [0.1, 0.1, 0.5]),
                     node('aa', 0, [0.7, 0.3], [0.8, 0.8, 0.8])];

        cycle(jji, roots);

        setInterval(function() {cycle(jji, roots);}, 1000);
    };

    return {
        ik: ik,
        above: above
    };
}();