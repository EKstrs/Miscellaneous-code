def baby_step_giant_step(public_key, primitive_root, prime):
    m = int(prime ** 0.5) + 1

    # Precompute the table of baby steps
    baby_steps = {}
    for j in range(m):
        baby_steps[pow(primitive_root, j, prime)] = j

    # Compute the giant step
    primitive_root_inverse_m = pow(primitive_root, -m, prime)
    giant_step = public_key

    # Check for a match in the table of baby steps
    for i in range(m):
        if giant_step in baby_steps:
            private_key = i * m + baby_steps[giant_step]
            return private_key
        giant_step = (giant_step * primitive_root_inverse_m) % prime

    print("Private Key not found.")
    return None


# Given Diffie-Hellman values
p = 829557623  # Prime
g = 2  # Primitive Root
Alice_public_key = 183680498  # Alice's Public Key
Bob_public_key = 815559957  # Bob's Public Key


Alice_private_key = baby_step_giant_step(Alice_public_key, g, p)
print("Private Key found:", Alice_private_key)

Bob_private_key = baby_step_giant_step(Bob_public_key,g, p)
print("Private Key found:", Bob_private_key)


Shared_secret_Bob = pow(Alice_public_key, Bob_private_key, p)
Shared_secret_Alice = pow(Bob_public_key, Alice_private_key, p)

print(Shared_secret_Alice)
print(Shared_secret_Bob)