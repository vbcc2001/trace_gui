from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import padding


def rsa_sign(path):
    with open(path,'rb') as key_file:
        private_key = serialization.load_pem_private_key(key_file.read(),password=None)

    message = b"A message I want to sign"
    signature = private_key.sign(
        message,
        padding.PSS(
            mgf=padding.MGF1(hashes.SHA256()),
            salt_length=padding.PSS.MAX_LENGTH
        ),
        hashes.SHA256()
    )

    print(signature)
    return signature


def rsa_verify(path,signature):
    with open(path,'rb') as key_file:
        public_key = serialization.load_pem_public_key(key_file.read())

    message = b"A message I want to sighh"
    return public_key.verify(
        signature,
        message,
        padding.PSS(
            mgf=padding.MGF1(hashes.SHA256()),
            salt_length=padding.PSS.MAX_LENGTH
        ),
        hashes.SHA256()
    )



if __name__ == '__main__':

    sig = rsa_sign('keys/trace-pda/key.pem')

    print(rsa_verify('keys/trace-pda/pubkey.pem', sig))
