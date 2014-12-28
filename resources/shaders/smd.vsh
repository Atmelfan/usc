
uniform {
    int parent;
    mat4 matrix;
}bones[16];

uniform mat4 model;

in int parent;
in vec4 position;
in vec4 normal;
in vec2 texcoord;

mat4 smd_bone_transform(int id){
    mat4 mat;
    while(id != -1){
        mat = bones[id].matrix*mat;
        id = bones[id].parent;
    }
    return model*mat;
}

